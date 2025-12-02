package com.zonefinder.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Result of a zone lookup containing all relevant information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneLookupResult implements Serializable {
    private String postcode;
    private String zone;
    private Integer zoneNumber;
    private String area;
    private Double latitude;
    private Double longitude;
    private Double distanceFromCentreKm;
    private FareInfo fareInfo;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FareInfo implements Serializable {
        private Double peakFare;
        private Double offPeakFare;
        private String currency;
    }

    /**
     * Internal helper record used by services when constructing a response.
     */
    public record ZoneInfo(String name, int number, double peakFare, double offPeakFare) {}
}