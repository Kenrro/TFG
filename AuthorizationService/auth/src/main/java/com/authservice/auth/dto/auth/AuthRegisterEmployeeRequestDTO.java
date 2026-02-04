package com.authservice.auth.dto.auth;


import com.authservice.auth.entity.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRegisterEmployeeRequestDTO {

    @NotBlank(message = "username is required")
    @Pattern(
        regexp = "^(\\+34)?[6789]\\d{8}$",
        message = "Número de teléfono español inválido"
    )
    @NotNull(message = "name is required")
    private String username;

    @NotBlank(message = "password is required")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
        message = "The password must be at least 8 characters long and include letters and numbers."
    )
    @NotNull(message = "name is required")
    private String password;

    @NotBlank(message = "name is required")
    @Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,30}$",
        message = "username invalid"
    )
    @NotNull(message = "name is required")
    private String name;

    @NotBlank(message = "lastname is required")
    @Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,30}$",
        message = "invalid last name"
    )
    @NotNull(message = "name is required")
    private String lastname;

    @NotNull(message = "rol is required")
    private Role role;
}
