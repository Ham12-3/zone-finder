package com.zonefinder.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Domain model representing a UK postcode with geographic coordinates
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Postcode implements Serializable {
    private String postcode;
    private Double latitude;
    private Double longitude;
    private String adminDistrict;
    private String region;
    private String country;
    
    /**
     * Normalize postcode format (remove spaces, uppercase)
     */
    public String getNormalizedPostcode() {
        return postcode != null ? postcode.replaceAll("\\s+", "").toUpperCase() : null;
    }
}