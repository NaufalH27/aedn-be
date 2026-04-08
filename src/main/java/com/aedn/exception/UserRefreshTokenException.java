package com.aedn.exception;

public class UserRefreshTokenException extends RuntimeException {
    public UserRefreshTokenException(String message) {
        super(message);
    }

    public UserRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
