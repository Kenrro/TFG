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
public class AuthUpdateCustomerRequestDto {


    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$|^$",
        message = "Invalid password"
    )
    private String password;

    @Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,30}$|^$",
        message = "Invalid name"
    )
    private String name;

    @Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,30}$|^$",
        message = "Invalid last name"
    )
    private String lastname;

    private Role role;
}
