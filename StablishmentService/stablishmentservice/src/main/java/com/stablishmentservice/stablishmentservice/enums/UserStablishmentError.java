package com.stablishmentservice.stablishmentservice.enums;

import org.springframework.http.HttpStatus;

public enum UserStablishmentError implements IError {
    USER_STABLISHMENT_RELATION_ALREADY_EXISTS(HttpStatus.CONFLICT, "User-Stablishment relation already exists"),
    USER_STABLISHMENT_RELATION_NOT_FOUND(HttpStatus.NOT_FOUND, "User-Stablishment relation not found"),
    USER_STABLISHMENT_RELATION_DELETION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete User-Stablishment relation"),
    INVALID_USER_STABLISHMENT_DATA(HttpStatus.BAD_REQUEST, "Invalid relation data provided."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating relation")
    ;

    private final String message;
    private final HttpStatus httpStatus;

    UserStablishmentError(HttpStatus httpStatus,
            String message) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
