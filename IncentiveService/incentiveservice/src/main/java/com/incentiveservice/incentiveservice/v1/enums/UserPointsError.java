package com.incentiveservice.incentiveservice.v1.enums;

import org.springframework.http.HttpStatus;


public enum UserPointsError implements IError {

    // Errores generales
    USER_POINTS_CREATION_FAILED(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Error creating user points"
    ),
    USER_POINTS_UPDATE_FAILED(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Error updating user points"
    ),
    USER_POINTS_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "User points not found"
    ),

    // Errores de balance
    INSUFFICIENT_USER_POINTS(
        HttpStatus.BAD_REQUEST,
        "User does not have enough points"
    ),
    INVALID_POINTS_AMOUNT(
        HttpStatus.BAD_REQUEST,
        "Invalid points amount"
    ),

    // Errores relacionados con transacciones
    POINTS_ALREADY_APPLIED(
        HttpStatus.CONFLICT,
        "Points already applied for this transaction"
    ),
    TRANSACTION_POINTS_FAILED(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Failed to apply points transaction"
    ),

    // Validación
    INVALID_USER_ID(
        HttpStatus.BAD_REQUEST,
        "Invalid user id"
    ),

    // Comunicación entre servicios
    SERVICE_COMMUNICATION_FAILED(
        HttpStatus.BAD_GATEWAY,
        "Failed to communicate with external service"
    ),

    // Error inesperado
    UNEXPECTED_ERROR(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Unexpected error occurred"
    );

    private final String message;
    private final HttpStatus httpStatus;
    UserPointsError(HttpStatus httpStatus, String message) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}

