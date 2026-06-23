package com.nutritrack.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.nutritrack.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ErrorResponse.builder()
                                .success(false)
                                .message(ex.getMessage())
                                .status(404)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        log.warn("Unauthorized access: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ErrorResponse.builder()
                                .success(false)
                                .message(ex.getMessage())
                                .status(401)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }

    @ExceptionHandler(EmailServiceException.class)
    public ResponseEntity<ErrorResponse> handleEmailServiceException(EmailServiceException ex) {
        log.error("Email service error: {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ErrorResponse.builder()
                                .success(false)
                                .message("Failed to send email. Please try again later.")
                                .status(500)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Bad credentials attempt");

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ErrorResponse.builder()
                                .success(false)
                                .message("Invalid email or password")
                                .status(401)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ErrorResponse.builder()
                                .success(false)
                                .message("Authentication failed")
                                .status(401)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        log.warn("Validation failed: {}", ex.getMessage());

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .success(false)
                                .message(message)
                                .status(400)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormat(InvalidFormatException ex) {
        log.warn("Invalid format: {}", ex.getMessage());

        String message = "Invalid format for field: " + ex.getPath().stream()
                .map(p -> p.getFieldName())
                .collect(Collectors.joining("."));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .success(false)
                                .message(message)
                                .status(400)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        log.error("Runtime error: {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .success(false)
                                .message(ex.getMessage())
                                .status(400)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception ex) {
        log.error("Unexpected error occurred", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ErrorResponse.builder()
                                .success(false)
                                .message("An unexpected error occurred")
                                .status(500)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }
}