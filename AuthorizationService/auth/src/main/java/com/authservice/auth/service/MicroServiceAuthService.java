package com.authservice.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.authservice.auth.dto.microservice.MicroServiceAuthRequestDto;
import com.authservice.auth.dto.microservice.MicroServiceAuthResponseDto;
import com.authservice.auth.jwt.JwtUtil;

@Service
public class MicroServiceAuthService {
    @Value("${app.jwt.secret.public}")
    private String publicKey;
    @Value("${app.servicescredential.password}")
    private String password;

    @Autowired
    private JwtUtil jwtUtil;
    public MicroServiceAuthResponseDto authenticationMicroService(MicroServiceAuthRequestDto requestDto) {
        if(!password.equals(requestDto.getPassword())) throw new RuntimeException("Stablisment failure");
        String token = jwtUtil.generateServiceToken(requestDto.getMicroserviceName());
        return MicroServiceAuthResponseDto.builder()
            .token(token)
            .publicKey(publicKey)
            .build();
    }
}
