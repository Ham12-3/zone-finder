package com.zonefinder.exception;

import com.zonefinder.dto.ZoneLookupResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for consistent error responses
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    /**
     * Handle validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
        MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
    
    /**
     * Handle postcode not found
     */
    @ExceptionHandler(PostcodeNotFoundException.class)
    public ResponseEntity<ZoneLookupResponse> handlePostcodeNotFound(
        PostcodeNotFoundException ex
    ) {
        log.warn("Postcode not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ZoneLookupResponse.error(ex.getMessage()));
    }
    
    /**
     * Handle illegal arguments
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ZoneLookupResponse> handleIllegalArgument(
        IllegalArgumentException ex
    ) {
        log.warn("Invalid argument: {}", ex.getMessage());
        return ResponseEntity.badRequest()
            .body(ZoneLookupResponse.error(ex.getMessage()));
    }
    
    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ZoneLookupResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.internalServerError()
            .body(ZoneLookupResponse.error("An unexpected error occurred"));
    }
}