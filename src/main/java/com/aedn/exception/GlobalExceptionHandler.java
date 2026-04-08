package com.aedn.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aedn.common.ApiResponse;

import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(Exception e) {
        log.error("INTERNAL SERVER ERROR", e);
        ApiResponse<Object> response = ApiResponse.failure(
                "Something went wrong Unexpectedly",
                "INTERNAL_SERVER_ERROR",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserCreation(UserCreationException e) {
        ApiResponse<Object> response = ApiResponse.failure(
                "User creation failed",
                "USER_CREATION_ERROR",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<Void>> handleJwtException(JwtException e) {
        ApiResponse<Void> body = ApiResponse.failure(
                "Invalid Jwt session",
                "INVALID_JWT",
                e.getMessage()
                );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }
    @ExceptionHandler(UserRefreshTokenException.class)
    public ResponseEntity<ApiResponse<Void>> handleRefreshTokenException(UserRefreshTokenException e) {
        ApiResponse<Void> body = ApiResponse.failure(
                "Invalid Refresh Token",
                "INVALID_REFRESH_TOKEN",
                e.getMessage()
                );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrity(DataIntegrityViolationException e) {

        log.error("Unhandled DataIntegrityViolationException, note for dev: make sure to handle the exception before reach the database.", e);

        ApiResponse<Object> response = ApiResponse.failure(
                "A database constraint was violated",
                "DATABASE_CONSTRAINT_ERROR",
                "note for dev: make sure to handle the exception before reach the database. there is unhandled exception related to database"
                );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(UserLoginException.class)
    public ResponseEntity<ApiResponse<Object>> handleLogin(UserLoginException e) {
        ApiResponse<Object> response = ApiResponse.failure(
                "User login failed",
                "USER_LOGIN_ERROR",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AuthorizationDeniedException e) {
        ApiResponse<Object> response = ApiResponse.failure(
                "Access denied",
                "FORBIDDEN",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuth(AuthenticationException e) {
        ApiResponse<Object> response = ApiResponse.failure(
                "Authentication required",
                "UNAUTHORIZED",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingRequestBody(HttpMessageNotReadableException e) {
        ApiResponse<Object> response = ApiResponse.failure(
                "invalid or empty request body",
                "REQUEST_BODY_EXCEPTION",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
