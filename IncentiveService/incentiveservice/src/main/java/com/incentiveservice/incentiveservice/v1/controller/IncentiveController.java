package com.incentiveservice.incentiveservice.v1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incentiveservice.incentiveservice.v1.dto.incentive.IncentiveCreateRequestDto;
import com.incentiveservice.incentiveservice.v1.dto.incentive.IncentiveUpdateDto;
import com.incentiveservice.incentiveservice.v1.dto.incentive.ProductIncentiveResponseDto;
import com.incentiveservice.incentiveservice.v1.dto.incentive.ProductsIncentivesDto;
import com.incentiveservice.incentiveservice.v1.service.incentive.IncentiveService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

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



//TODO: connect with another services

@RestController
@RequestMapping("/v1/incentives")
@RequiredArgsConstructor
@Tag(name = "Incentives", description = "Operations related to incentives and user rewards")
public class IncentiveController {

    private final IncentiveService incentiveService;

    // ========================= GET INCENTIVES BY STABLISHMENT =========================

    @Operation(
        summary = "Get incentives by stablishment code",
        description = "Returns the list of products with their incentives for a given stablishment"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Incentives retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Stablishment not found"),
        @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SELLER') or hasAuthority('CUSTOMER')")
    @GetMapping("/get-by-stablishment-code/{stablishmentCode}")
    public ResponseEntity<ProductsIncentivesDto> getByStablishmentCode(
        @PathVariable String stablishmentCode
    ) {
        ProductsIncentivesDto incentives =
            incentiveService.getIncentive(stablishmentCode);
        return ResponseEntity.ok(incentives);
    }

    // ========================= GET INCENTIVE BY ID =========================

    @Operation(
        summary = "Get incentive by id",
        description = "Returns a single incentive by its identifier"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Incentive retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Incentive not found"),
        @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SELLER') or hasAuthority('CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductIncentiveResponseDto> getById(
        @PathVariable Long id
    ) {
        ProductIncentiveResponseDto incentive =
            incentiveService.getIncentiveById(id);
        return ResponseEntity.ok(incentive);
    }

    // ========================= CREATE INCENTIVE =========================

    @Operation(
        summary = "Create incentive",
        description = "Creates a new incentive for a product"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Incentive created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid incentive data"),
        @ApiResponse(responseCode = "409", description = "Product already has an incentive"),
        @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> create(
        @RequestBody IncentiveCreateRequestDto requestDto
    ) {
        incentiveService.createIncentive(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // ========================= UPDATE INCENTIVE =========================

    @Operation(
        summary = "Update incentive",
        description = "Updates an existing incentive by its id"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Incentive updated successfully"),
        @ApiResponse(responseCode = "404", description = "Incentive not found"),
        @ApiResponse(responseCode = "400", description = "Invalid incentive data"),
        @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
        @PathVariable Long id,
        @RequestBody IncentiveUpdateDto entity,
        @RequestHeader("Authorization") String token
    ) {
        incentiveService.updateIncentive(entity, id, token);
        return ResponseEntity.ok().build();
    }

    // ========================= DELETE INCENTIVE BY ID =========================

    @Operation(
        summary = "Delete incentive by id",
        description = "Deletes an incentive by its identifier"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Incentive deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Incentive not found"),
        @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
        @PathVariable Long id,
        @RequestHeader("Authorization") String token
    ) {
        incentiveService.deleteById(id, token);
        return ResponseEntity.ok().build();
    }

    // ========================= DELETE INCENTIVE BY PRODUCT =========================

    @Operation(
        summary = "Delete incentive by product id",
        description = "Deletes the incentive associated with a product (internal service use)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Incentive deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Incentive not found"),
        @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    @PreAuthorize("hasAuthority('SERVICE')")
    @DeleteMapping("/delete-by-product-id/{productId}")
    public ResponseEntity<Void> deleteByProductId(
        @PathVariable Long productId
    ) {
        incentiveService.deleteByProductId(productId);
        return ResponseEntity.ok().build();
    }

    // ========================= DELETE INCENTIVES BY STABLISHMENT =========================

    @Operation(
        summary = "Delete incentives by stablishment code",
        description = "Deletes all incentives associated with a stablishment (internal service use)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Incentives deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Stablishment not found"),
        @ApiResponse(responseCode = "500", description = "Unexpected error")
    })
    @PreAuthorize("hasAuthority('SERVICE')")
    @DeleteMapping("/delete-by-stablishment/{stablishmentCode}")
    public ResponseEntity<Void> deleteByCode(
        @PathVariable String stablishmentCode
    ) {
        incentiveService.deleteAllByStablishmentCode(stablishmentCode);
        return ResponseEntity.ok().build();
    }
}

