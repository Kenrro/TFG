package com.transactionservice.transactionservice.v1.dto.authorization;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PublicKeyAndTokenResponseDto {
    private String publicKey;
    private String token;
}
