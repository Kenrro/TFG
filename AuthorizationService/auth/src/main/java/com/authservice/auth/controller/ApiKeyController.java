package com.authservice.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api-key")
public class ApiKeyController {
    
    @Value("${app.jwt.secret.public}")
    private String publicKey;

    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("/get-public-key")
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
    public String getPublicKey() {
        return publicKey;
    }
    
    
}
