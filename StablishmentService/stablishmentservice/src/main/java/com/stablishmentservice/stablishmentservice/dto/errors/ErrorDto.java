package com.stablishmentservice.stablishmentservice.dto.errors;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDto {
    private LocalDate timestamp;
    private String status;
    private String message;
}
