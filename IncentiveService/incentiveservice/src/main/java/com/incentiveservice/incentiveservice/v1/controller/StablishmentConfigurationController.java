package com.incentiveservice.incentiveservice.v1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incentiveservice.incentiveservice.v1.dto.stablishmentconfiguration.StablishmentConfigurationCreateRequestDto;
import com.incentiveservice.incentiveservice.v1.dto.stablishmentconfiguration.StablishmentConfigurationResponseDto;
import com.incentiveservice.incentiveservice.v1.dto.stablishmentconfiguration.StablishmentConfigurationUpdateRequestDto;
import com.incentiveservice.incentiveservice.v1.service.stablishmentconfiguration.StablishmentConfigurationService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    // =========================================================
    // GET STABLISHMENT CONFIGURATION
    // =========================================================
    @Operation(
        summary = "Get stablishment configuration",
        description = "Returns the points configuration for a specific stablishment. Only accessible by ADMIN users.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Configuration retrieved successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StablishmentConfigurationResponseDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Configuration response",
                            summary = "Example configuration",
                            value = """
                            {
                            "stablishmentCode": "EST-9F3A2B",
                            "pointsPerEuro": 10
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(responseCode = "404", description = "Stablishment configuration not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
        }
    )
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<StablishmentConfigurationResponseDto> getConfiguration(
            @RequestHeader("Authorization")
            @Parameter(description = "Bearer token")
            String token
    ) {

        return ResponseEntity.ok(
                stablishmentConfigurationService.getByStablishmentCode(token)
        );
    }
    // =========================================================
    // CREATE STABLISHMENT CONFIGURATION
    // =========================================================
    @Operation(
        summary = "Create stablishment configuration",
        description = "Creates a new points configuration for a stablishment. Intended for internal service usage.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Configuration created successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StablishmentConfigurationResponseDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Create configuration response",
                            summary = "Example created configuration",
                            value = """
                            {
                            "stablishmentCode": "EST-9F3A2B",
                            "pointsPerEuro": 10
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(responseCode = "409", description = "Configuration already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid configuration data")
        }
    )
    @Hidden
    @PreAuthorize("hasAuthority('SERVICE')")
    @PostMapping
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Stablishment configuration creation data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = StablishmentConfigurationCreateRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "Create configuration",
                    summary = "Valid configuration request",
                    value = """
                    {
                    "stablishmentCode": "EST-9F3A2B",
                    "pointsPerEuro": 10
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<StablishmentConfigurationResponseDto> createConfiguration(
            @RequestBody StablishmentConfigurationCreateRequestDto request
    ) {

        StablishmentConfigurationResponseDto response =
                stablishmentConfigurationService.createStablishmentConfiguration(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =========================================================
    // UPDATE CONFIGURATION
    // =========================================================
    @Operation(
        summary = "Update stablishment configuration",
        description = "Updates the points-per-euro configuration for a stablishment.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Configuration updated successfully"),
            @ApiResponse(responseCode = "404", description = "Configuration not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
        }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Updated stablishment configuration data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = StablishmentConfigurationUpdateRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "Update configuration",
                    summary = "Valid update request",
                    value = """
                    {
                    "point_per_euro": 15
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Void> updateConfiguration(
            @RequestBody StablishmentConfigurationUpdateRequestDto request,
            @RequestHeader("Authorization")
            @Parameter(description = "Bearer token")
            String token
    ) {

        stablishmentConfigurationService.updateStablishmentConfiguration(request, token);
        return ResponseEntity.ok().build();
    }

    // =========================================================
    // DELETE STABLISHMENT CONFIGURATION
    // =========================================================
    @Operation(
        summary = "Delete stablishment configuration",
        description = "Deletes the points configuration of a stablishment.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Configuration deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Configuration not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
        }
    )
    @Hidden
    @PreAuthorize("hasAuthority('SERVICE')")
    @DeleteMapping("/{stablishmentCode}")
    public ResponseEntity<Void> deleteConfiguration(
            @PathVariable
            @Parameter(description = "Stablishment code", example = "EST-9F3A2B")
            String stablishmentCode
    ) {

        stablishmentConfigurationService
                .deleteStablishmentConfiguration(stablishmentCode);

        return ResponseEntity.ok().build();
    }

}
