package com.zonefinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main Spring Boot Application
 * Entry point for the Zone Finder Backend
 */
@SpringBootApplication
@EnableCaching
public class ZoneFinderApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ZoneFinderApplication.class, args);
    }
}