package com.authservice.auth.dto.auth;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(
        regexp = "^\\+?[1-9]\\d{7,14}$",
        message = "Número de teléfono inválido"
    )
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial"
    )
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,30}$",
        message = "Nombre inválido"
    )
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,30}$",
        message = "Apellido inválido"
    )
    private String lastname;
}
