package com.authservice.auth.dto.microservice;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MicroServiceAuthResponseDto {
    private String token;
    private String publicKey;
}
