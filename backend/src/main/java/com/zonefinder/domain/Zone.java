package com.zonefinder.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * JPA Entity representing a TfL zone boundary
 * Stores zone information for efficient lookup
 */
@Entity
@Table(name = "zones", indexes = {
    @Index(name = "idx_zone_number", columnList = "zone_number"),
    @Index(name = "idx_postcode_prefix", columnList = "postcode_prefix")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Zone {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer zoneNumber;
    
    @Column(nullable = false)
    private String zoneName; // e.g., "Zone 1", "Zone 2"
    
    // Postcode prefix for quick lookup (e.g., "SW1A", "E1")
    @Column(length = 10)
    private String postcodePrefix;
    
    // Central point for distance calculation
    private Double centralLatitude;
    private Double centralLongitude;
    
    // Distance range from central London (in km)
    private Double minDistanceKm;
    private Double maxDistanceKm;
    
    // Fare information
    private Double peakFare;
    private Double offPeakFare;
    
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}