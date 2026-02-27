package com.auth.exception;

public class AuthException extends Exception {

    private final ErrorCode errorCode;

    public AuthException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public AuthException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() { return errorCode; }

    public enum ErrorCode {
        EMAIL_ALREADY_EXISTS,
        INVALID_CREDENTIALS,
        VALIDATION_ERROR,
        DATABASE_ERROR,
        INTERNAL_ERROR
    }
}
