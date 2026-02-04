package com.transactionservice.transactionservice.v1.exception;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.transactionservice.transactionservice.v1.dto.errors.ErrorDto;


@RestControllerAdvice
public class GlobalExceptionHandler {

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


}
