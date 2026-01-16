package com.authservice.auth.dto.stablisment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StablishmentRoleRequest {
    private String establishmentCode;
    private long userId;
}
