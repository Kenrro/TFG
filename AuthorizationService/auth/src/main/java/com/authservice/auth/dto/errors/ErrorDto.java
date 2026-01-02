package com.authservice.auth.dto.errors;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ErrorDto {
    private LocalDate timestamp;
    private HttpStatus status;
    private String message;
}
