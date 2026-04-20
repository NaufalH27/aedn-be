package com.aedn.exception;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException e) {

        List<String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .toList();

        ApiResponse<Object> response = ApiResponse.failure(
                "VALIDATION_ERROR",
                "Invalid request payload",
                errors
                );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ObjectStorageUnavailableException.class)
    public ResponseEntity<ApiResponse<Object>> handleObjStrUnv(ObjectStorageUnavailableException e) {
        ApiResponse<Object> response = ApiResponse.failure(
                "Object storage unavailable or out of reach, Please try again later",
                "OBJ_STORE_UNAVAILABLE",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(NoHandlerFoundException e) {
        ApiResponse<Object> response = ApiResponse.failure(
                "404 - Not Found",
                "NOT_FOUND",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        ApiResponse<Object> response = ApiResponse.failure(
                "405 - Method not allowed",
                "METHOD_NOT_ALLOWED",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(NoResourceFoundException e) {
        ApiResponse<Object> response = ApiResponse.failure(
                "404 - Not Found",
                "NOT_FOUND",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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
                "Database integrity and constraint unhandled"
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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFound(UserNotFoundException e) {
        ApiResponse<Object> response = ApiResponse.failure(
                "User not found",
                "USER_NOT_FOUND",
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

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleProductNotFound(ProductNotFoundException e) {
        ApiResponse<Object> response = ApiResponse.failure(
                "Product not found",
                "PRODUCT_NOT_FOUND",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
