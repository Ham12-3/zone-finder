package com.zonefinder.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Utility class for postcode validation
 * Ensures UK postcode format compliance
 */
@Component
public class PostcodeValidator {
    
    // Comprehensive UK postcode regex
    private static final Pattern POSTCODE_PATTERN = Pattern.compile(
        "^[A-Z]{1,2}\\d{1,2}[A-Z]?\\s?\\d[A-Z]{2}$",
        Pattern.CASE_INSENSITIVE
    );
    
    /**
     * Validate UK postcode format
     * @param postcode The postcode to validate
     * @return true if valid, false otherwise
     */
    public boolean isValid(String postcode) {
        if (postcode == null || postcode.trim().isEmpty()) {
            return false;
        }
        return POSTCODE_PATTERN.matcher(postcode.trim()).matches();
    }
    
    /**
     * Normalize postcode (remove spaces, uppercase)
     * @param postcode The postcode to normalize
     * @return Normalized postcode
     */
    public String normalize(String postcode) {
        if (postcode == null) {
            return null;
        }
        return postcode.replaceAll("\\s+", "").toUpperCase();
    }
    
    /**
     * Format postcode with proper spacing (e.g., SW1A1AA -> SW1A 1AA)
     * @param postcode The postcode to format
     * @return Formatted postcode
     */
    public String format(String postcode) {
        if (postcode == null) {
            return null;
        }
        String normalized = normalize(postcode);
        if (normalized.length() < 5) {
            return normalized;
        }
        // Insert space before last 3 characters
        int splitPoint = normalized.length() - 3;
        return normalized.substring(0, splitPoint) + " " + normalized.substring(splitPoint);
    }
}