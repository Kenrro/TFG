package com.authservice.auth.dto.auth;

import com.authservice.auth.entity.Role;

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
public class AuthUpdateEmployeeRequestDto {

    @Pattern(
        regexp = "^(\\+34)?[6789]\\d{8}$",
        message = "invalid username"
    )
    @NotNull
    private String username;

    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$|^$",
        message = "Invalid password"
    )
    @NotNull
    private String password;

    @Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,30}$|^$",
        message = "Invalid name"
    )
    @NotNull
    private String name;

    @Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,30}$|^$",
        message = "Invalid last name"
    )
    @NotNull
    private String lastname;

    // Para enums no se usa regex
    @NotNull
    private Role role;
}
