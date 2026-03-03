package com.stablishmentservice.stablishmentservice.jwt;

import java.lang.String;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.stablishmentservice.stablishmentservice.dto.authorization.PublicKeyAndTokenRequestDto;
import com.stablishmentservice.stablishmentservice.dto.authorization.PublicKeyAndTokenResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
@RequiredArgsConstructor
@Slf4j
public class PublicKeyLoader {

    private final JwtUtil jwtUtil;

    @Qualifier("publicWebClient")
    private final WebClient publicWebClient;

    @Value("${app.servicescredential.password}")
    private String apiPassword;

    @Value("${app.servicescredential.apiUrl}")
    private String apiUrl;

    private final String microserviceName = "stablishmentservice";

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info("🔐 Initializing PublicKeyLoader for service [{}]", microserviceName);
        retryLoadPublicKey();
    }

    private void retryLoadPublicKey() {
        Mono.defer(this::loadPublicKey)
            .retryWhen(
                Retry.backoff(Long.MAX_VALUE, Duration.ofSeconds(5))
                    .doBeforeRetry(r ->
                        log.warn("⏳ Auth-service not ready. Retrying in 5s...")
                    )
            )
            .subscribe(
                unused -> log.info("✅ Auth handshake completed successfully"),
                error -> log.error("❌ Unexpected error during Auth handshake", error)
            );
    }

    private Mono<Void> loadPublicKey() {
        return publicWebClient
            .post()
            .uri(apiUrl)
            .bodyValue(buildRequest())
            .retrieve()
            .bodyToMono(PublicKeyAndTokenResponseDto.class)
            .doOnNext(response -> {
                configureJwt(response);
                log.info(
                    "🎉 SUCCESS → Public key & service token loaded for [{}]",
                    microserviceName
                );
            })
            .then();
    }

    private PublicKeyAndTokenRequestDto buildRequest() {
        return PublicKeyAndTokenRequestDto.builder()
            .microserviceName(microserviceName)
            .password(apiPassword)
            .build();
    }

    private void configureJwt(PublicKeyAndTokenResponseDto response) {
        try {
            String publicKeyStr = response.getPublicKey()
                .replaceAll("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");

            PublicKey publicKey = kf.generatePublic(spec);

            jwtUtil.setPublicKey(publicKey);
            jwtUtil.setServiceToken(response.getToken());

            log.info("🔑 JWT keys configured successfully");

        } catch (Exception e) {
            throw new IllegalStateException("Failed to configure JWT", e);
        }
    }
}


