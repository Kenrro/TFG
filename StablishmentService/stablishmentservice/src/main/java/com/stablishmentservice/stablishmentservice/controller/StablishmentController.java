package com.stablishmentservice.stablishmentservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentAndAdminRequestDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentAndAdminResponseDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentDashboardResponseDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentRequestDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentResponseDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentWithConfigurationResponseDto;
import com.stablishmentservice.stablishmentservice.entity.Stablishment;
import com.stablishmentservice.stablishmentservice.service.stablishment.StablishmentService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/stablishments")
@RequiredArgsConstructor
@Tag(name = "Establishment Controller", description = "Endpoints for managing establishments")
public class StablishmentController {

    private final StablishmentService stablishmentService;

    // =========================================================
    // GET ALL STABLISHMENTS
    // =========================================================
    @GetMapping
    @Operation(
        summary = "Get all establishments",
        description = "Retrieve a list of all establishments.",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of establishments retrieved successfully")
        }
    )
    
    public ResponseEntity<List<Stablishment>> getAllStablishments() {
        return ResponseEntity.ok(stablishmentService.getAllStablishments());
    }
    // =========================================================
    // CREATE STABLISHMENT
    // =========================================================
    @PostMapping
    @Operation(
        summary = "Create a new establishment",
        description = "Create a new establishment along with its admin user."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Establishment created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = StablishmentAndAdminResponseDto.class),
                examples = @ExampleObject(
                    name = "Success response",
                    value = """
                    {
                    "name": "Sazón Lempira",
                    "description": "Restaurante de comida mediterránea",
                    "address": "Calle Mayor 25, Madrid",
                    "code": "SAZ-0001"
                    }
                    """
                )
            )
        )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Create stablishmetn",
        required = true,
        content = @Content(
            schema = @Schema(implementation = StablishmentRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "Create establishment",
                    summary = "Valid create request",
                    value = """
                {
                "stablishment": {
                    "name": "Sazón lempira",
                    "address": "Calle Carpo y Torta 2",
                    "phone": "+34604808080",
                    "email": "sazon@gmial.com",
                    "description": "Restaurante típico Hondureño con 4 años de existir"
                },
                "adminUser": {
                    "username": "666889922",
                    "password": "Password3@",
                    "name": "Misahel",
                    "lastname": "Peña"
                }
                }
                    """
                )
            }
        )
    )
    public ResponseEntity<StablishmentAndAdminResponseDto> createStablishment(
            @RequestBody
            @Parameter(description = "Establishment and admin user details")
            @Valid StablishmentAndAdminRequestDto stablishmentAndAdminRequestDto) {

        StablishmentAndAdminResponseDto response =
                stablishmentService.createStablishmentWithAdmin(
                        stablishmentAndAdminRequestDto.getStablishment(),
                        stablishmentAndAdminRequestDto.getAdminUser()
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =========================================================
    // UPDATE STABLISHMENT
    // =========================================================
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping
    @Operation(
        summary = "Update an establishment",
        description = "Update an existing establishment. Requires ADMIN role.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Establishment updated successfully"),
            @ApiResponse(responseCode = "404", description = "Establishment not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN role required")
        }
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Updated establishment details",
        required = true,
        content = @Content(
            schema = @Schema(implementation = StablishmentRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "Update establishment",
                    summary = "Valid update request",
                    value = """
                    {
                        "name": "Sazón lempira",
                        "address": "Calle Carpo y Torta 2",
                        "phone": "+34604808080",
                        "email": "sazon@gmial.com",
                        "description": "Restaurante típico Hondureño con años de existir"
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Void> updateStablishment(
            @RequestHeader("Authorization") String token,
            @RequestBody StablishmentRequestDto body
    ) {

        stablishmentService.updateStablishment(body, token);
        return ResponseEntity.ok().build();
    }


    // =========================================================
    // DELETE STABLISHMENT
    // =========================================================
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping
    @Operation(
        summary = "Delete an establishment",
        description = "Delete an establishment using the authorization token. Requires ADMIN role.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Establishment deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN role required")
        }
    )
    public ResponseEntity<Void> deleteStablishment(
            @RequestHeader("Authorization") @Parameter(description = "Bearer token for authentication") String authHeader) {

        stablishmentService.deleteStablishment(authHeader);
        return ResponseEntity.ok().build();
    }

    // =========================================================
    // GET STABLISHMENT CODE BY USER ID
    // =========================================================
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("/{userId}/stablishment-code")
    @Operation(
        summary = "Get establishment code by user ID",
        description = "Retrieve the establishment code associated with a given user ID. Requires SERVICE role.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Establishment code retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User or establishment not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - SERVICE role required")
        }
    )
    public ResponseEntity<String> getStablishmentCodeByUserId(
            @PathVariable @Parameter(description = "User ID") Long userId) {

        return ResponseEntity.ok(stablishmentService.getStablishmentCodeByUserId(userId));
    }

    // =========================================================
    // GET STABLISHMENT(S) BY TOKEN
    // =========================================================
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/stablishments/me")
    @Operation(
        summary = "Get establishments for the current user",
        description = "Retrieve establishments associated with the current user from the token. Requires CUSTOMER role.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Establishments retrieved successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StablishmentResponseDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Establishments list",
                            summary = "Example response",
                            value = """
                            [
                            {
                                "name": "Restaurante El Buen Sabor",
                                "address": "Calle Mayor 25, Madrid",
                                "phone": "+34678123456",
                                "email": "contacto@buensabor.com",
                                "description": "Restaurante de comida mediterránea"
                            },
                            {
                                "name": "Café Central",
                                "address": "Plaza España 3, Madrid",
                                "phone": "+34911222333",
                                "email": "info@cafecentral.com",
                                "description": "Cafetería tradicional"
                            }
                            ]
                            """
                        )
                    }
                )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - CUSTOMER role required")
        }
    )
    public ResponseEntity<List<StablishmentResponseDto>> getStablishmentByToken(
            @RequestHeader("Authorization")
            @Parameter(description = "Bearer token for authentication")
            String authHeader
    ) {

        List<StablishmentResponseDto> stablishments =
                stablishmentService.getStablishmentsByToken(authHeader);

        return ResponseEntity.ok(stablishments);
    }
    // =========================================================
    // GET STABLISHMENT(S) BY CODE
    // =========================================================
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{code}/stablishments")
    @Operation(
        summary = "Get establishment by code",
        description = "Retrieve an establishment using its unique code. Requires SERVICE role.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Establishment retrieved successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StablishmentResponseDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Establishment response",
                            summary = "Example establishment",
                            value = """
                            {
                            "name": "Restaurante El Buen Sabor",
                            "address": "Calle Mayor 25, Madrid",
                            "phone": "+34678123456",
                            "email": "contacto@buensabor.com",
                            "description": "Restaurante de comida mediterránea"
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - SERVICE role required"),
            @ApiResponse(responseCode = "404", description = "Establishment not found")
        }
    )
    public ResponseEntity<StablishmentResponseDto> getStablishmentByCode(
            @PathVariable
            @Parameter(description = "Unique establishment code", example = "EST-9F3A2B")
            String code
    ) {

        StablishmentResponseDto response =
                stablishmentService.getStablishmentByCode(code);

        return ResponseEntity.ok(response);
    }
    // =========================================================
    // GET STABLISHMENT WITH ROLE ADMIN
    // =========================================================
    @Operation(
        summary = "Get establishment and configuration for admin",
        description = "Retrieves the establishment information along with its configuration based on the authenticated ADMIN user.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Establishment and configuration retrieved successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StablishmentWithConfigurationResponseDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Establishment with configuration",
                            summary = "Example response",
                            value = """
                            {
                            "stablishment": {
                                "name": "My Restaurant",
                                "description": "Best burgers in town",
                                "address": "123 Main Street",
                                "code": "SAZ-0001"
                            },
                            "configuration": {
                                "stablishmentCode": "SAZ-0001",
                                "points_per_euro": 10
                            }
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - ADMIN role required"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Unexpected error"
            )
        }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/admin/stablishment")
    public ResponseEntity<StablishmentWithConfigurationResponseDto> getStablishmentFromAdmin(
        @RequestHeader("Authorization")
        @Parameter(description = "Bearer token for authentication")
        String token
    ) {
        StablishmentWithConfigurationResponseDto response = stablishmentService.getStablishmentsByTokenAdmin(token);
        return ResponseEntity.ok(response);
    }
    // =========================================================
    // GET DASHBOARD
    // =========================================================
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/dashboard")
    @Operation(
        summary = "Get establishment dashboard information",
        description = "Retrieve dashboard information for the establishment associated with the current user. Requires ADMIN role.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Dashboard information retrieved successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StablishmentDashboardResponseDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Dashboard response",
                            summary = "Example dashboard information",
                            value = """
                            {
                                "usersQuantity": {
                                    "totalUsers": 150,
                                    "activeUsers": 120,
                                    "inactiveUsers": 30
                                },
                                "transactionInformation": {
                                    "redeemedProducts": 500,
                                    "pointsAwarded": 2500
                                },
                                "productsQuantity": 50,
                                "incentiveQuantity": 10
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN role required")
        }
    )
    public ResponseEntity<StablishmentDashboardResponseDto> getStablishmentDashboard(
            @RequestHeader("Authorization")
            @Parameter(description = "Bearer token for authentication")
            String authHeader
    ) {

        StablishmentDashboardResponseDto dashboardInfo =
                stablishmentService.getStablishmentDashboard(authHeader);

        return ResponseEntity.ok(dashboardInfo);
    }
    
}