package com.incentiveservice.incentiveservice.v1.enums;

import org.springframework.http.HttpStatus;

public enum AuthorizationError implements IError {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid or missing authentication token."),

    ;
    private final String message;
    private final HttpStatus httpStatus;

    AuthorizationError(HttpStatus httpStatus, String message) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public org.springframework.http.HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
