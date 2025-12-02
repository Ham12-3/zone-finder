package com.zonefinder.exception;

/**
 * Exception thrown when a postcode is not found
 */
public class PostcodeNotFoundException extends RuntimeException {
    public PostcodeNotFoundException(String message) {
        super(message);
    }
}