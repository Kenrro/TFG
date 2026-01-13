package com.authservice.auth.dto.microservice;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MicroServiceAuthRequestDto {
    @NotBlank(message = "microservicename is mandatory")
    private String microserviceName;
    @NotBlank(message = "password id mandatory")
    private String password;
}
