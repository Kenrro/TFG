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
        regexp = "^\\+?[1-9]\\d{7,14}$",
        message = "Invalid username"
    )
    private String username;

    @NotBlank(message = "password is required")
    @Pattern(
        regexp = "^(?=.*[A-ZÁÉÍÓÚÑ])(?=.*[a-záéíóúñ])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-zÁÉÍÓÚÑáéíóúñ\\d@$!%*?&]{8,}$",
        message = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String password;

    @NotBlank(message = "name is required")
    @Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,30}$",
        message = "username invalid"
    )
    private String name;

    @NotBlank(message = "lastname is required")
    @Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,30}$",
        message = "invalid last name"
    )
    private String lastname;

    @NotNull(message = "rol is required")
    private Role role;
}
