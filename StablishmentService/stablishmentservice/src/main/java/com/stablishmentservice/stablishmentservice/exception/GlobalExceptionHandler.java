package com.stablishmentservice.stablishmentservice.exception;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import com.stablishmentservice.stablishmentservice.dto.errors.ErrorDto;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(StablishmentGeneralException.class)
    public ResponseEntity<ErrorDto> handleStablishmentGeneralException(StablishmentGeneralException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .from(ex.getFrom())
                .timestamp(LocalDate.now())
                .status(String.valueOf(ex.getHttpStatus().value())) // como string numérico
                .message(ex.getMessage())
                .build();
        System.out.println(errorDto);
        return ResponseEntity.status(ex.getHttpStatus()).body(errorDto);
    }

}