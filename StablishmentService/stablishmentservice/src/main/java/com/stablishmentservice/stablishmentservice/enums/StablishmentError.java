package com.stablishmentservice.stablishmentservice.enums;

import org.springframework.http.HttpStatus;


public enum StablishmentError implements IError {
    // Errores generales
    STABLISHMENT_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating the establishment"),
    STABLISHMENT_DELETION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting the establishment"),
    STABLISHMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Establishment not found"),
    
    // Errores de código
    STABLISHMENT_CODE_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate unique establishment code"),
    STABLISHMENT_CODE_ALREADY_EXISTS(HttpStatus.CONFLICT, "Establishment code already exists"),
    
    // Errores relacionados con admin
    ADMIN_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create admin in Auth Service"),
    ADMIN_ROLLBACK_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to rollback admin creation in Auth Service"),
    
    // Errores de relación usuario-establecimiento
    USER_STABLISHMENT_RELATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user-establishment relation"),
    
    // Validación
    INVALID_STABLISHMENT_DATA(HttpStatus.BAD_REQUEST, "Invalid establishment data"),
    INVALID_ADMIN_DATA(HttpStatus.BAD_REQUEST, "Invalid admin data");

    private final String message;
    private final HttpStatus httpStatus;

    StablishmentError(HttpStatus httpStatus, String message) {
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
