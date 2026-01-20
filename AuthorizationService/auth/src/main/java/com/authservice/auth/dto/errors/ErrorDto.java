package com.authservice.auth.dto.errors;

import java.time.LocalDate;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ErrorDto {
    private String from;
    private LocalDate timestamp;
    private String status;
    private String message;
}
