package com.incentiveservice.incentiveservice.v1.enums;

import org.springframework.http.HttpStatus;

public enum StablishmentConfigurationError implements IError {

    STABLISHMENT_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "El establecimiento no existe"
    ),

    STABLISHMENT_CONFIGURATION_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "La configuración del establecimiento no existe"
    ),

    INVALID_STABLISHMENT_CODE(
        HttpStatus.BAD_REQUEST,
        "El código del establecimiento es inválido"
    ),

    STABLISHMENT_ALREADY_CONFIGURED(
        HttpStatus.CONFLICT,
        "El establecimiento ya tiene una configuración activa"
    ),

    STABLISHMENT_CONFIGURATION_DISABLED(
        HttpStatus.FORBIDDEN,
        "La configuración del establecimiento está deshabilitada"
    ),

    INSUFFICIENT_POINTS(
        HttpStatus.BAD_REQUEST,
        "El usuario no tiene puntos suficientes para esta operación"
    ),

    INVALID_CONFIGURATION_VALUE(
        HttpStatus.BAD_REQUEST,
        "El valor de configuración es inválido"
    ),

    STABLISHMENT_CONFIGURATION_UPDATE_NOT_ALLOWED(
        HttpStatus.FORBIDDEN,
        "No está permitido modificar esta configuración"
    ),

    USER_NOT_AUTHORIZED(
        HttpStatus.FORBIDDEN,
        "El usuario no tiene permisos para realizar esta acción"
    ),

    INTERNAL_STABLISHMENT_CONFIGURATION_ERROR(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Error interno en la configuración del establecimiento"
    );

    private final HttpStatus status;
    private final String message;

    StablishmentConfigurationError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return status;
    }
}
