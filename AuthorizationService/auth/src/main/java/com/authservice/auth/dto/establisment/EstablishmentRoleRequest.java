package com.authservice.auth.dto.establisment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstablishmentRoleRequest {
    private String establishmentCode;
    private long userId;
}
