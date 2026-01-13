package com.authservice.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.authservice.auth.dto.microservice.MicroServiceAuthRequestDto;
import com.authservice.auth.dto.microservice.MicroServiceAuthResponseDto;
import com.authservice.auth.service.MicroServiceAuthService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api-key")
public class ApiKeyController {
    
    @Autowired
    private MicroServiceAuthService microServiceAuthService;

    @PostMapping("/get-public-key")
    @Operation(
        summary = "Get Public Key",
        description = "Endpoint to retrieve the public key for JWT verification.",
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Public key retrieved successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "403",
                description = "Access denied"
            )
        }
    )
    public ResponseEntity<MicroServiceAuthResponseDto> getPublicKey(@RequestBody @Valid MicroServiceAuthRequestDto request) {
        MicroServiceAuthResponseDto response = microServiceAuthService.authenticationMicroService(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    
}
