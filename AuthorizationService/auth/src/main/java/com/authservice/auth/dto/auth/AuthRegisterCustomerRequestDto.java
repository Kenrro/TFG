package com.authservice.auth.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRegisterCustomerRequestDto {

    @NotBlank(message = "username is required")
    @Pattern(
        regexp = "^(\\+34)?[6789]\\d{8}$",
        message = "Invalid username"
    )
    @NotNull(message = "username is required")
    private String username;

    @NotBlank(message = "password id required")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
        message = "The password must be at least 8 characters long and include letters and numbers."
    )
    @NotNull(message = "password id required")
    private String password;

    @NotBlank(message = "Name is required")
    @Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,30}$",
        message = "Invalid name"
    )
    @NotNull(message = "Name is required")
    private String name;

    @NotBlank(message = "Last name is required")
    @Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,30}$",
        message = "Invalid lastname"
    )
    @NotBlank(message = "Last name is required")
    private String lastname;
}
