package com.stablishmentservice.stablishmentservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentAndAdminRequestDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentAndAdminResponseDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentRequestDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentResponseDto;
import com.stablishmentservice.stablishmentservice.entity.Stablishment;
import com.stablishmentservice.stablishmentservice.service.stablishment.StablishmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    // GET ALL ESTABLISHMENTS
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
    // CREATE ESTABLISHMENT
    // =========================================================
    @PostMapping
    @Operation(
        summary = "Create a new establishment",
        description = "Create a new establishment along with its admin user.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Establishment created successfully")
        }
    )
    public ResponseEntity<StablishmentAndAdminResponseDto> createStablishment(
            @RequestBody @Parameter(description = "Establishment and admin user details") StablishmentAndAdminRequestDto stablishmentAndAdminRequestDto) {

        StablishmentAndAdminResponseDto response = stablishmentService.createStablishmentWithAdmin(
                stablishmentAndAdminRequestDto.getStablishment(),
                stablishmentAndAdminRequestDto.getAdminUser()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =========================================================
    // UPDATE ESTABLISHMENT
    // =========================================================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    @Operation(
        summary = "Update an establishment",
        description = "Update an existing establishment by ID. Requires ADMIN role.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Establishment updated successfully"),
            @ApiResponse(responseCode = "404", description = "Establishment not found")
        }
    )
    public ResponseEntity<Void> updateStablishment(
            @RequestHeader("Authorization") String token,
            @RequestBody @Parameter(description = "Updated establishment details") StablishmentRequestDto body) {

        stablishmentService.updateStablishment(body, token);
        return ResponseEntity.ok().build();
    }

    // =========================================================
    // DELETE ESTABLISHMENT
    // =========================================================
    @PreAuthorize("hasRole('ADMIN')")
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
    // GET ESTABLISHMENT CODE BY USER ID
    // =========================================================
    @PreAuthorize("hasRole('SERVICE')")
    @GetMapping("/get-stablishment-code/{userId}")
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
    // GET ESTABLISHMENT(S) BY TOKEN
    // =========================================================
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/get-stablihsment-by-token")
    @Operation(
        summary = "Get establishments for the current user",
        description = "Retrieve establishments associated with the current user from the token. Requires CUSTOMER role.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Establishments retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - CUSTOMER role required")
        }
    )
    public ResponseEntity<List<StablishmentResponseDto>> getStablishmentByToken(
            @RequestHeader("Authorization") @Parameter(description = "Bearer token for authentication") String authHeader) {

        List<StablishmentResponseDto> stablishments = stablishmentService.getStablishmentsByToken(authHeader);
        return ResponseEntity.ok(stablishments);
    }
    // =========================================================
    // GET ESTABLISHMENT(S) BY CODE
    // =========================================================
    @GetMapping("/get-stablishment-by-code/{code}")
    public ResponseEntity<StablishmentResponseDto> getStablishmentByCode(@PathVariable String code) {
        StablishmentResponseDto response = stablishmentService.getStablishmentByCode(code);
        return ResponseEntity.ok(response);
    }
    
}