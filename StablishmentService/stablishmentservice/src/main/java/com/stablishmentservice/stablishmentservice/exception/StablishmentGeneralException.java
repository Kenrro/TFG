package com.stablishmentservice.stablishmentservice.exception;

import org.springframework.http.HttpStatus;

import com.stablishmentservice.stablishmentservice.dto.errors.ErrorDto;
import com.stablishmentservice.stablishmentservice.enums.IError;

import lombok.Data;

@Data
public class StablishmentGeneralException extends RuntimeException {
    private final String from;
    private final String message;
    private final HttpStatus httpStatus;
    
    public <T extends IError> StablishmentGeneralException(T error) {
        super(error.getMessage());
        this.from = "Stablishment service";
        this.httpStatus = error.getHttpStatus();
        this.message = error.getMessage();
    }   
    public StablishmentGeneralException(ErrorDto errorDto) {
        super(errorDto.getMessage());
        this.from = errorDto.getFrom();
        this.message = errorDto.getMessage();
        this.httpStatus = HttpStatus.resolve(parseStatus(errorDto.getStatus()));
    }

    // Método auxiliar para parsear
    private int parseStatus(String status) {
        try {
            // Si es un número: "404" -> 404
            return Integer.parseInt(status);
        } catch (NumberFormatException e) {
            // Si es un nombre: "NOT_FOUND" -> HttpStatus.NOT_FOUND.value()
            try {
                return HttpStatus.valueOf(status).value();
            } catch (IllegalArgumentException ex) {
                // default a INTERNAL_SERVER_ERROR si falla
                return HttpStatus.INTERNAL_SERVER_ERROR.value();
            }
        }
    }

}
