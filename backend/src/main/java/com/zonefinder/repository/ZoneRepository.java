package com.zonefinder.repository;

import com.zonefinder.domain.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Zone entity
 * Handles database operations for zone data
 */
@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    
    /**
     * Find zone by exact zone number
     */
    Optional<Zone> findByZoneNumber(Integer zoneNumber);
    
    /**
     * Find zone by postcode prefix
     */
    Optional<Zone> findByPostcodePrefix(String postcodePrefix);
    
    /**
     * Find zone by distance range
     */
    @Query("SELECT z FROM Zone z WHERE :distance >= z.minDistanceKm AND :distance < z.maxDistanceKm")
    Optional<Zone> findByDistanceRange(@Param("distance") Double distance);
}