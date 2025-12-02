package com.zonefinder.dto;

import com.zonefinder.domain.ZoneLookupResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for zone lookup endpoint
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneLookupResponse {
    private boolean success;
    private ZoneLookupResult data;
    private String message;
    private Long timestamp;
    
    public static ZoneLookupResponse success(ZoneLookupResult result) {
        return ZoneLookupResponse.builder()
            .success(true)
            .data(result)
            .timestamp(System.currentTimeMillis())
            .build();
    }
    
    public static ZoneLookupResponse error(String message) {
        return ZoneLookupResponse.builder()
            .success(false)
            .message(message)
            .timestamp(System.currentTimeMillis())
            .build();
    }
}