package com.zonefinder.controller;

import com.zonefinder.domain.ZoneLookupResult;
import com.zonefinder.dto.ZoneLookupRequest;
import com.zonefinder.dto.ZoneLookupResponse;
import com.zonefinder.service.ZoneLookupService;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for zone lookup operations
 * Exposes HTTP endpoints for the frontend
 */
@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins:http://localhost:3000}")
public class ZoneController {
    
    private final ZoneLookupService zoneLookupService;
    
    /**
     * Lookup zone by postcode
     * POST /api/zones/lookup
     * 
     * @param request Zone lookup request containing postcode
     * @return Zone lookup response with all details
     */
    @PostMapping("/lookup")
    @Timed(value = "zone.lookup", description = "Time taken to lookup zone")
    public ResponseEntity<ZoneLookupResponse> lookupZone(@Valid @RequestBody ZoneLookupRequest request) {
        log.info("Received zone lookup request for postcode: {}", request.getPostcode());
        
        try {
            ZoneLookupResult result = zoneLookupService.lookupZone(request.getPostcode());
            return ResponseEntity.ok(ZoneLookupResponse.success(result));
            
        } catch (IllegalArgumentException e) {
            log.warn("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ZoneLookupResponse.error(e.getMessage()));
                
        } catch (Exception e) {
            log.error("Error processing zone lookup", e);
            return ResponseEntity.internalServerError()
                .body(ZoneLookupResponse.error("Internal server error. Please try again later."));
        }
    }
    
    /**
     * Get zone by postcode (Alternative GET endpoint)
     * GET /api/zones/{postcode}
     */
    @GetMapping("/{postcode}")
    @Timed(value = "zone.get", description = "Time taken to get zone")
    public ResponseEntity<ZoneLookupResponse> getZone(@PathVariable String postcode) {
        log.info("Received GET request for postcode: {}", postcode);
        
        try {
            ZoneLookupResult result = zoneLookupService.lookupZone(postcode);
            return ResponseEntity.ok(ZoneLookupResponse.success(result));
            
        } catch (Exception e) {
            log.error("Error getting zone", e);
            return ResponseEntity.internalServerError()
                .body(ZoneLookupResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Health check endpoint
     * GET /api/zones/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Zone Finder API is running");
    }
}