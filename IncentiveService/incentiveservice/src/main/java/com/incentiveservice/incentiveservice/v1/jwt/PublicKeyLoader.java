package com.incentiveservice.incentiveservice.v1.jwt;

import java.lang.String;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.incentiveservice.incentiveservice.v1.dto.authorization.PublicKeyAndTokenRequestDto;
import com.incentiveservice.incentiveservice.v1.dto.authorization.PublicKeyAndTokenResponseDto;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublicKeyLoader {

    final private JwtUtil jwtUtil;
    @Value("${app.servicescredential.password}")
    private String apiPassword;
    @Value("${app.servicescredential.apiUrl}")
    private String apiUrl;
    @Qualifier("publicWebClient")
    final private WebClient publicWebClient;
    private String microserviceName = "incentiveservice";

    @PostConstruct
    public void loadPublicKey() throws Exception {
        PublicKeyAndTokenResponseDto response = getResponse();
        String publicKeyStr = response.getPublicKey()
        .replaceAll("-----BEGIN PUBLIC KEY-----", "")
        .replaceAll("-----END PUBLIC KEY-----", "")
        .replaceAll("\\s", ""); // elimina todos los espacios y saltos de línea
        System.out.println("Public key and service token loaded successfully."+response.getPublicKey());
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(spec);
        jwtUtil.setPublicKey(publicKey);
        jwtUtil.setServiceToken(response.getToken());
    }
    private PublicKeyAndTokenResponseDto getResponse(){
        PublicKeyAndTokenRequestDto request = PublicKeyAndTokenRequestDto.builder()
         .microserviceName(microserviceName)
         .password(apiPassword)
         .build();

        return publicWebClient
            .post()
            .uri(apiUrl)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(PublicKeyAndTokenResponseDto.class)
            .block();
    }
}
