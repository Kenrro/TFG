package com.stablishmentservice.stablishmentservice.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.stablishmentservice.stablishmentservice.dto.errors.ErrorDto;

import lombok.extern.slf4j.Slf4j;


@RestControllerAdvice
@Slf4j
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
    @Override // Handle for validations
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            // Si ya hay un error para ese campo, no lo sobrescribe
            errors.putIfAbsent(error.getField(), error.getDefaultMessage());
        });

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("reasons", errors);
        body.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGeneric(Exception ex) {
        log.error("Unhandled exception", ex);

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorDto.builder()
                    .from("Internal service from stablishment service")
                    .timestamp(LocalDate.now())
                    .status("500")
                    .message("Unexpected error")
                    .build()
            );
}

}