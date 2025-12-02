package com.zonefinder.client;

import com.zonefinder.domain.Postcode;
import com.zonefinder.exception.PostcodeNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

/**
 * Client for Postcodes.io API
 * Handles all external API calls with circuit breaker and retry logic
 */
@Component
@Slf4j
public class PostcodesIoClient {
    
    private final WebClient webClient;
    
    public PostcodesIoClient(
        @Value("${postcodes.api.base-url:https://api.postcodes.io}") String baseUrl
    ) {
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }
    
    /**
     * Fetch postcode data from Postcodes.io API
     * Includes caching, circuit breaker, and retry logic
     * 
     * @param postcode The postcode to look up
     * @return Postcode domain object
     * @throws PostcodeNotFoundException if postcode not found
     */
    @Cacheable(value = "postcodes", key = "#postcode", unless = "#result == null")
    @CircuitBreaker(name = "postcodesApi", fallbackMethod = "fetchPostcodeFallback")
    @Retry(name = "postcodesApi")
    public Postcode fetchPostcode(String postcode) {
        log.info("Fetching postcode data for: {}", postcode);
        
        try {
            Map<String, Object> response = webClient
                .get()
                .uri("/postcodes/{postcode}", postcode)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
            
            if (response == null || !response.containsKey("result")) {
                throw new PostcodeNotFoundException("Postcode not found: " + postcode);
            }
            
            Map<String, Object> result = (Map<String, Object>) response.get("result");
            
            return Postcode.builder()
                .postcode((String) result.get("postcode"))
                .latitude((Double) result.get("latitude"))
                .longitude((Double) result.get("longitude"))
                .adminDistrict((String) result.get("admin_district"))
                .region((String) result.get("region"))
                .country((String) result.get("country"))
                .build();
                
        } catch (WebClientResponseException.NotFound e) {
            log.warn("Postcode not found: {}", postcode);
            throw new PostcodeNotFoundException("Postcode not found: " + postcode);
        } catch (Exception e) {
            log.error("Error fetching postcode: {}", postcode, e);
            throw new RuntimeException("Failed to fetch postcode data", e);
        }
    }
    
    /**
     * Fallback method when circuit breaker is open
     */
    private Postcode fetchPostcodeFallback(String postcode, Exception e) {
        if (e instanceof PostcodeNotFoundException pne) {
            // Propagate functional 404 errors without wrapping so the controller can respond with 404
            throw pne;
        }
        log.error("Circuit breaker fallback triggered for postcode: {}", postcode, e);
        throw new RuntimeException("Postcode service temporarily unavailable. Please try again later.");
    }
}