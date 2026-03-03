package com.authservice.auth.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import com.authservice.auth.dto.errors.ErrorDto;


@ControllerAdvice
public class GlabalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GeneralException.class)
    private ResponseEntity<?> authExceptionHandler(
        GeneralException ex,
        WebRequest request
    ) {
        ErrorDto errorDto = ErrorDto.builder()
            .from(ex.getFrom())
            .timestamp(java.time.LocalDate.now())
            .status(String.valueOf(ex.getHttpStatus().value())) // como string numérico
            .message(ex.getMessage())
            .build();
        return ResponseEntity.status(ex.getHttpStatus()).body(errorDto);
    }
    @Override
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
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorDto.builder()
                    .from("Internal service")
                    .timestamp(LocalDate.now())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .message("Unexpected error")
                    .build()
            );
    }
}
