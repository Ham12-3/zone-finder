package com.zonefinder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for zone lookup endpoint
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneLookupRequest {
    
    @NotBlank(message = "Postcode is required")
    @Pattern(
        regexp = "^[A-Z]{1,2}\\d{1,2}[A-Z]?\\s?\\d[A-Z]{2}$",
        message = "Invalid UK postcode format",
        flags = Pattern.Flag.CASE_INSENSITIVE
    )
    private String postcode;
}