package com.zonefinder.util;

import org.springframework.stereotype.Component;

/**
 * Utility class for zone calculations
 * Uses Haversine formula for distance calculation
 */
@Component
public class ZoneCalculator {
    
    // Central London coordinates (Charing Cross)
    private static final double CENTRAL_LAT = 51.5074;
    private static final double CENTRAL_LON = -0.1278;
    
    // Earth's radius in kilometers
    private static final double EARTH_RADIUS_KM = 6371.0;
    
    /**
     * Calculate distance from central London
     * @param latitude Target latitude
     * @param longitude Target longitude
     * @return Distance in kilometers
     */
    public double calculateDistanceFromCentre(double latitude, double longitude) {
        return calculateDistance(CENTRAL_LAT, CENTRAL_LON, latitude, longitude);
    }
    
    /**
     * Calculate distance between two geographic points using Haversine formula
     * @param lat1 First point latitude
     * @param lon1 First point longitude
     * @param lat2 Second point latitude
     * @param lon2 Second point longitude
     * @return Distance in kilometers
     */
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS_KM * c;
    }
    
    /**
     * Determine zone number based on distance from centre
     * @param distanceKm Distance in kilometers
     * @return Zone number (1-9)
     */
    public int determineZoneNumber(double distanceKm) {
        if (distanceKm < 3) return 1;
        if (distanceKm < 6) return 2;
        if (distanceKm < 10) return 3;
        if (distanceKm < 15) return 4;
        if (distanceKm < 20) return 5;
        if (distanceKm < 25) return 6;
        if (distanceKm < 35) return 7;
        if (distanceKm < 45) return 8;
        return 9;
    }
    
    /**
     * Get zone name from zone number
     * @param zoneNumber Zone number
     * @return Zone name (e.g., "Zone 1")
     */
    public String getZoneName(int zoneNumber) {
        return "Zone " + zoneNumber;
    }
}