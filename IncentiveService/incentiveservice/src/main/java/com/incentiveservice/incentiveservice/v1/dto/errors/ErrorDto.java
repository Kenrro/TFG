package com.incentiveservice.incentiveservice.v1.dto.errors;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {
    private String from;
    private LocalDate timestamp;
    private String status;
    private String message;
}
