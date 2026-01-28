package com.incentiveservice.incentiveservice.v1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incentiveservice.incentiveservice.v1.dto.stablishmentconfiguration.StablishmentConfigurationCreateRequestDto;
import com.incentiveservice.incentiveservice.v1.dto.stablishmentconfiguration.StablishmentConfigurationResponseDto;
import com.incentiveservice.incentiveservice.v1.dto.stablishmentconfiguration.StablishmentConfigurationUpdateRequestDto;
import com.incentiveservice.incentiveservice.v1.service.stablishmentconfiguration.StablishmentConfigurationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/v1/stablishment-configurations")
@RequiredArgsConstructor
@Tag(name = "Stablishment Configuration", description = "Manage point configuration per stablishment")
public class StablishmentConfigurationController {

    private final StablishmentConfigurationService stablishmentConfigurationService;

    @Operation(
        summary = "Get stablishment configuration",
        description = "Returns the points configuration for a specific stablishment. Only accessible by ADMIN users."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Configuration retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Stablishment configuration not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<StablishmentConfigurationResponseDto> getConfiguration(
        @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(
            stablishmentConfigurationService.getByStablishmentCode(token)
        );
    }

    @Operation(
        summary = "Create stablishment configuration",
        description = "Creates a new points configuration for a stablishment. Intended to be used internally by services."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Configuration created successfully"),
        @ApiResponse(responseCode = "409", description = "Configuration already exists"),
        @ApiResponse(responseCode = "400", description = "Invalid configuration data")
    })
    @PreAuthorize("hasAuthority('SERVICE')")
    @PostMapping
    public ResponseEntity<StablishmentConfigurationResponseDto> createConfiguration(
        @RequestBody StablishmentConfigurationCreateRequestDto request
    ) {
        StablishmentConfigurationResponseDto response = stablishmentConfigurationService.createStablishmentConfiguration(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Update stablishment configuration",
        description = "Updates the points-per-euro configuration for a stablishment."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Configuration updated successfully"),
        @ApiResponse(responseCode = "404", description = "Configuration not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    public ResponseEntity<Void> updateConfiguration(
        @RequestBody StablishmentConfigurationUpdateRequestDto request,
        @RequestHeader("Authorization") String token
    ) {
        stablishmentConfigurationService.updateStablishmentConfiguration(
            request, token
        );
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Delete stablishment configuration",
        description = "Deletes the points configuration of a stablishment."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Configuration deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Configuration not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasAuthority('SERVICE')")
    @DeleteMapping("/{stablishmentCode}")
    public ResponseEntity<Void> deleteConfiguration(
        @PathVariable String stablishmentCode
    ) {
        stablishmentConfigurationService.deleteStablishmentConfiguration(stablishmentCode);
        return ResponseEntity.ok().build();
    }
}
