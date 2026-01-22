package com.productservice.productsservice.exception;

import org.springframework.http.HttpStatus;

import com.productservice.productsservice.dto.errors.ErrorDto;
import com.productservice.productsservice.enums.IError;

import lombok.Data;

@Data
public class ProductGeneralException extends RuntimeException{
    private final String from;
    private final HttpStatus httpStatus;
    
    public <T extends IError> ProductGeneralException(T error) {
        super(error.getMessage());
        this.from = "Product service";
        this.httpStatus = error.getHttpStatus();
    }   
    public ProductGeneralException(ErrorDto errorDto) {
        super(errorDto.getMessage());
        this.from = errorDto.getFrom();
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
