package com.authservice.auth.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthLoginEmployeeRequestDto {

    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^\\+?[1-9]\\d{7,14}$",
        message = "Invalid phone number format"
    )
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Establishment code is required")
    @Pattern(
        regexp = "^[A-Z]{1,3}-\\d{4}$",
        message = "Invalid establishment code format (expected: ABC-0001)"
    )
    private String establishmentCode;
}
