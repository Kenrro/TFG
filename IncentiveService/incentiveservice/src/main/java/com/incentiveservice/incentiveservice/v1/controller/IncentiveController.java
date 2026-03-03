package com.incentiveservice.incentiveservice.v1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incentiveservice.incentiveservice.v1.dto.incentive.IncentiveCreateRequestDto;
import com.incentiveservice.incentiveservice.v1.dto.incentive.IncentiveUpdateDto;
import com.incentiveservice.incentiveservice.v1.dto.incentive.ProductIncentiveResponseDto;
import com.incentiveservice.incentiveservice.v1.dto.incentive.ProductsIncentivesDto;
import com.incentiveservice.incentiveservice.v1.service.incentive.IncentiveService;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/v1/incentives")
@RequiredArgsConstructor
@Tag(name = "Incentives", description = "Operations related to incentives and user rewards")
public class IncentiveController {

    private final IncentiveService incentiveService;

    // ========================= GET INCENTIVES BY STABLISHMENT =========================
    @Operation(
        summary = "Get incentives by stablishment code",
        description = "Returns the list of products with their incentives for a given stablishment.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Incentives retrieved successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductsIncentivesDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Incentives response",
                            summary = "Example incentives response",
                            value = """
                            {
                            "stablishmentCode": "EST-9F3A2B",
                            "products": [
                                {
                                "productId": 10,
                                "name": "Hamburguesa Clásica",
                                "originalPrice": 9.99,
                                "incentivePrice": 7.99,
                                "discountPercentage": 20
                                },
                                {
                                "productId": 11,
                                "name": "Pizza Margarita",
                                "originalPrice": 12.50,
                                "incentivePrice": 10.00,
                                "discountPercentage": 20
                                }
                            ]
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Stablishment not found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Unexpected error"
            )
        }
    )
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SELLER') or hasAuthority('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/get-by-stablishment-code/{stablishmentCode}")
    public ResponseEntity<ProductsIncentivesDto> getByStablishmentCode(
            @PathVariable
            @Parameter(description = "Code of the stablishment", example = "EST-9F3A2B")
            String stablishmentCode
    ) {

        ProductsIncentivesDto incentives =
                incentiveService.getIncentive(stablishmentCode);

        return ResponseEntity.ok(incentives);
    }


    // ========================= GET INCENTIVE BY ID =========================
    @Operation(
        summary = "Get incentive by id",
        description = "Returns a single incentive by its identifier.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Incentive retrieved successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductIncentiveResponseDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Incentive response",
                            summary = "Example incentive",
                            value = """
                            {
                            "id": 5,
                            "productId": 10,
                            "productName": "Hamburguesa Clásica",
                            "originalPrice": 9.99,
                            "incentivePrice": 7.99,
                            "discountPercentage": 20,
                            "stablishmentCode": "EST-9F3A2B"
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Incentive not found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Unexpected error"
            )
        }
    )
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SELLER') or hasAuthority('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<ProductIncentiveResponseDto> getById(
            @PathVariable
            @Parameter(description = "ID of the incentive", example = "5")
            Long id
    ) {

        ProductIncentiveResponseDto incentive =
                incentiveService.getIncentiveById(id);

        return ResponseEntity.ok(incentive);
    }


    // ========================= CREATE INCENTIVE =========================
    @Operation(
        summary = "Create incentive",
        description = "Creates a new incentive for a product.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Incentive created successfully"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid incentive data"
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Product already has an incentive"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Unexpected error"
            )
        }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Incentive creation data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = IncentiveCreateRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "Create incentive example",
                    summary = "Valid incentive creation request",
                    value = """
                    {
                        "productId": 2,
                        "stablishmentCode": "SAZ-0001",
                        "pointsRequired": 150
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Void> create(
            @RequestBody IncentiveCreateRequestDto requestDto
    ) {

        incentiveService.createIncentive(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    // ========================= UPDATE INCENTIVE =========================
    @Operation(
        summary = "Update incentive",
        description = "Updates an existing incentive by its id.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Incentive updated successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Incentive not found"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid incentive data"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Unexpected error"
            )
        }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Incentive update data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = IncentiveUpdateDto.class),
            examples = {
                @ExampleObject(
                    name = "Update incentive example",
                    summary = "Valid incentive update request",
                    value = """
                    {
                    "poinstRequired": 200
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Void> update(
            @PathVariable
            @Parameter(description = "ID of the incentive to update", example = "5")
            Long id,

            @RequestBody IncentiveUpdateDto entity,

            @RequestHeader("Authorization")
            @Parameter(description = "Bearer token for authentication")
            String token
    ) {

        incentiveService.updateIncentive(entity, id, token);
        return ResponseEntity.ok().build();
    }

    // ========================= DELETE INCENTIVE BY ID =========================
    @Operation(
        summary = "Delete incentive by id",
        description = "Deletes an incentive by its identifier.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Incentive deleted successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Incentive not found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Unexpected error"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - ADMIN role required"
            )
        }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable
            @Parameter(description = "ID of the incentive to delete", example = "5")
            Long id,

            @RequestHeader("Authorization")
            @Parameter(description = "Bearer token for authentication")
            String token
    ) {

        incentiveService.deleteById(id, token);
        return ResponseEntity.ok().build();
    }


    // ========================= DELETE INCENTIVE BY PRODUCT =========================
    @Operation(
        summary = "Delete incentive by product id",
        description = "Deletes the incentive associated with a product. Intended for internal SERVICE usage.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Incentive deleted successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Incentive not found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Unexpected error"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - SERVICE role required"
            )
        }
    )
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete-by-product-id/{productId}")
    public ResponseEntity<Void> deleteByProductId(
            @PathVariable
            @Parameter(description = "ID of the product whose incentive will be deleted", example = "10")
            Long productId
    ) {

        incentiveService.deleteByProductId(productId);
        return ResponseEntity.ok().build();
    }


    // ========================= DELETE INCENTIVES BY STABLISHMENT =========================
    @Operation(
        summary = "Delete incentives by stablishment code",
        description = "Deletes all incentives associated with a stablishment. Intended for internal SERVICE usage.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Incentives deleted successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Stablishment not found"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Unexpected error"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - SERVICE role required"
            )
        }
    )
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete-by-stablishment/{stablishmentCode}")
    public ResponseEntity<Void> deleteByCode(
            @PathVariable
            @Parameter(description = "Stablishment code", example = "EST-9F3A2B")
            String stablishmentCode
    ) {

        incentiveService.deleteAllByStablishmentCode(stablishmentCode);
        return ResponseEntity.ok().build();
    }
}

