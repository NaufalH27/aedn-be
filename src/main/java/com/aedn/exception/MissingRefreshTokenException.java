package com.aedn.exception;

public class MissingRefreshTokenException extends RuntimeException {
    public MissingRefreshTokenException(String message) {
        super(message);
    }

    public MissingRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
