package com.incentiveservice.incentiveservice.v1.enums;

import org.springframework.http.HttpStatus;

public enum IncentiveError implements IError {

    // ===== ERRORES GENERALES =====
    INCENTIVE_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating incentive"),
    INCENTIVE_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating incentive"),
    INCENTIVE_DELETION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting incentive"),
    INCENTIVE_NOT_FOUND(HttpStatus.NOT_FOUND, "Incentive not found"),

    // ===== USER POINTS =====
    USER_POINTS_NOT_FOUND(HttpStatus.NOT_FOUND, "User points not found"),
    INSUFFICIENT_POINTS(HttpStatus.BAD_REQUEST, "User does not have enough points"),
    USER_POINTS_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update user points"),

    // ===== STABLISHMENT CONFIGURATION =====
    STABLISHMENT_CONFIGURATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Stablishment configuration not found"),
    INVALID_STABLISHMENT_CONFIGURATION(HttpStatus.BAD_REQUEST, "Invalid stablishment configuration"),

    // ===== TRANSACTIONS / QR =====
    TRANSACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Transaction not found"),
    TRANSACTION_ALREADY_PROCESSED(HttpStatus.CONFLICT, "Transaction already processed"),
    TRANSACTION_EXPIRED(HttpStatus.GONE, "Transaction expired"),
    INVALID_QR_CODE(HttpStatus.BAD_REQUEST, "Invalid QR code"),

    // ===== VALIDACIONES =====
    INVALID_INCENTIVE_DATA(HttpStatus.BAD_REQUEST, "Invalid incentive data"),

    // ===== COMUNICACIÓN ENTRE SERVICIOS =====
    SERVICE_COMMUNICATION_FAILED(HttpStatus.BAD_GATEWAY, "Failed to communicate with external service"),

    // ===== ERROR INESPERADO =====
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred"),
    PRODUCT_ALREADY_REGISTERED_AS_INCENTIVE(
    HttpStatus.CONFLICT, "Product is already registered as an incentive for this establishment"),
    USER_NOT_AUTHORIZED(
        HttpStatus.FORBIDDEN,
        "El usuario no tiene permisos para realizar esta acción"
    ),
    ;
    
    private final String message;
    private final HttpStatus httpStatus;

    IncentiveError(HttpStatus httpStatus, String message) {
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
