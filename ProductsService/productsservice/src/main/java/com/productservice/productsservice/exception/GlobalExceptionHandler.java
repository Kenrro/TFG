package com.productservice.productsservice.exception;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import com.productservice.productsservice.dto.errors.ErrorDto;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ErrorDto> handleStablishmentGeneralException(GeneralException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .from(ex.getFrom())
                .timestamp(LocalDate.now())
                .status(String.valueOf(ex.getHttpStatus().value())) // como string numérico
                .message(ex.getMessage())
                .build();
        System.out.println(errorDto);
        return ResponseEntity.status(ex.getHttpStatus()).body(errorDto);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGeneric(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorDto.builder()
                    .from("Internal service from product service")
                    .timestamp(LocalDate.now())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .message("Unexpected error")
                    .build()
            );
    }
}
