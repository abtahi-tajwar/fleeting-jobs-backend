package com.fleetingtrails.fleetingjobsbackend.common.exception;

import com.fleetingtrails.fleetingjobsbackend.common.response.APIErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIErrorResponse> handleResourceNotFound (ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(APIErrorResponse.of(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIErrorResponse> handleValidationErrors (MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(APIErrorResponse.of("Validation failed", errors));
    }
}
