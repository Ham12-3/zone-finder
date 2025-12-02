package com.zonefinder.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Loads the modelled fare-zone data produced by the GLA "MyLondon" project.
 * The dataset maps Output Areas (OA11CD) to fare zones and each postcode
 * district (outcode) to an Output Area. We join both files to create an
 * outcode -> fare-zone lookup that can be used while TfL's live API is
 * unavailable.
 */
@Component
@Slf4j
public class ZoneDataService {

    private final Resource fareZoneResource;
    private final Resource postcodeResource;
    private final Map<String, String> outcodeToZone = new HashMap<>();

    public ZoneDataService(
        @Value("classpath:data/MyLondon_fare_zone_OA.csv") Resource fareZoneResource,
        @Value("classpath:data/MyLondon_postcode_OA.csv") Resource postcodeResource
    ) {
        this.fareZoneResource = fareZoneResource;
        this.postcodeResource = postcodeResource;
    }

    @PostConstruct
    void loadData() {
        try {
            Map<String, String> oaToZone = loadOaZones();
            loadOutcodeZones(oaToZone);
            log.info("Loaded {} postcode districts with fare zones from MyLondon dataset", outcodeToZone.size());
        } catch (IOException ex) {
            log.error("Failed to load MyLondon fare zone datasets", ex);
        }
    }

    public Optional<String> findZoneForPostcode(String postcode) {
        String outcode = extractOutcode(postcode);
        if (outcode == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(outcodeToZone.get(outcode));
    }

    private Map<String, String> loadOaZones() throws IOException {
        Map<String, String> oaToZone = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
            fareZoneResource.getInputStream(), StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",", -1);
                if (columns.length < 2) {
                    continue;
                }
                String oa = columns[0].trim();
                String zone = columns[1].trim();
                if (!oa.isEmpty() && !zone.isEmpty()) {
                    oaToZone.put(oa, zone);
                }
            }
        }
        log.info("Loaded {} OA -> fare zone mappings", oaToZone.size());
        return oaToZone;
    }

    private void loadOutcodeZones(Map<String, String> oaToZone) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
            postcodeResource.getInputStream(), StandardCharsets.UTF_8))) {
            String line = reader.readLine(); // header
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",", -1);
                if (columns.length < 2) {
                    continue;
                }
                String oa = columns[0].trim();
                String outcode = columns[1].trim().toUpperCase();
                if (outcode.isEmpty()) {
                    continue;
                }
                String zone = oaToZone.get(oa);
                if (zone != null) {
                    outcodeToZone.putIfAbsent(outcode, zone);
                }
            }
        }
    }

    private String extractOutcode(String postcode) {
        if (postcode == null) {
            return null;
        }
        String trimmed = postcode.trim().toUpperCase();
        if (trimmed.isEmpty()) {
            return null;
        }
        int spaceIndex = trimmed.indexOf(' ');
        return spaceIndex > 0 ? trimmed.substring(0, spaceIndex) : trimmed;
    }
}

