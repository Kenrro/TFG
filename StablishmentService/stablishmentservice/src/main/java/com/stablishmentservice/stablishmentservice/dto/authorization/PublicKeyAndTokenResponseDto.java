package com.stablishmentservice.stablishmentservice.dto.authorization;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PublicKeyAndTokenResponseDto {
    private String publicKey;
    private String token;
}
