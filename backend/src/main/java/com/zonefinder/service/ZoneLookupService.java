package com.zonefinder.service;

import com.zonefinder.client.PostcodesIoClient;
import com.zonefinder.domain.Postcode;
import com.zonefinder.domain.ZoneLookupResult;
import com.zonefinder.util.PostcodeValidator;
import com.zonefinder.util.ZoneCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZoneLookupService {

    private final PostcodesIoClient postcodesIoClient;
    private final ZoneDataService zoneDataService;
    private final ZoneCalculator zoneCalculator;
    private final PostcodeValidator postcodeValidator;

    @Cacheable(value = "zoneLookups", key = "#postcode")
    public ZoneLookupResult lookupZone(String postcode) {
        log.info("Looking up zone for postcode: {}", postcode);

        if (!postcodeValidator.isValid(postcode)) {
            throw new IllegalArgumentException("Invalid postcode format: " + postcode);
        }

        Postcode postcodeData = postcodesIoClient.fetchPostcode(postcode);

        double distanceKm = zoneCalculator.calculateDistanceFromCentre(
            postcodeData.getLatitude(),
            postcodeData.getLongitude()
        );

        ZoneLookupResult.ZoneInfo zoneInfo = zoneDataService.findZoneForPostcode(postcodeData.getPostcode())
            .map(this::buildZoneFromDataset)
            .orElseGet(() -> buildCalculatedZone(distanceKm));

        return ZoneLookupResult.builder()
            .postcode(postcodeValidator.format(postcodeData.getPostcode()))
            .zone(zoneInfo.name())
            .zoneNumber(zoneInfo.number())
            .area(postcodeData.getAdminDistrict())
            .latitude(postcodeData.getLatitude())
            .longitude(postcodeData.getLongitude())
            .distanceFromCentreKm(Math.round(distanceKm * 10.0) / 10.0)
            .fareInfo(
                ZoneLookupResult.FareInfo.builder()
                    .peakFare(zoneInfo.peakFare())
                    .offPeakFare(zoneInfo.offPeakFare())
                    .currency("GBP")
                    .build()
            )
            .build();
    }

    private ZoneLookupResult.ZoneInfo buildCalculatedZone(double distanceKm) {
        int zoneNumber = zoneCalculator.determineZoneNumber(distanceKm);
        return new ZoneLookupResult.ZoneInfo(
            zoneCalculator.getZoneName(zoneNumber),
            zoneNumber,
            2.50 + zoneNumber * 0.50,
            2.00 + zoneNumber * 0.40
        );
    }

    private ZoneLookupResult.ZoneInfo buildZoneFromDataset(String zoneCode) {
        int primaryZone = parsePrimaryZone(zoneCode);
        if (primaryZone == 0) {
            return buildCalculatedZone(0);
        }
        String zoneName = "Zone " + zoneCode;
        return new ZoneLookupResult.ZoneInfo(
            zoneName,
            primaryZone,
            2.50 + primaryZone * 0.50,
            2.00 + primaryZone * 0.40
        );
    }

    private int parsePrimaryZone(String zoneCode) {
        if (zoneCode == null) {
            return 0;
        }
        String[] tokens = zoneCode.split("[^0-9]");
        for (String token : tokens) {
            if (!token.isBlank()) {
                try {
                    return Integer.parseInt(token);
                } catch (NumberFormatException ignored) {
                    // continue
                }
            }
        }
        return 0;
    }
}