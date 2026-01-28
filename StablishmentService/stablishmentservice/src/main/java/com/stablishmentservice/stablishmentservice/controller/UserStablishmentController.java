package com.stablishmentservice.stablishmentservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stablishmentservice.stablishmentservice.dto.authorization.AddRelationUserWithStablishmentRequestDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.UserStablishmentCreateCustomerRelationRequestDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.UserStablishmentResponseDto;
import com.stablishmentservice.stablishmentservice.service.userStablishment.UserStablishmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/user-stablishments")
@RequiredArgsConstructor
public class UserStablishmentController {

    private final UserStablishmentService userStablishmentService;

    // =========================================================
    // CREATE USER ↔ ESTABLISHMENT RELATION
    // =========================================================
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/add-relation-customer-stablishment")
    @Operation(
        summary = "Add relation between user and establishment",
        description = "Creates a relation between a user and an establishment.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Relation created successfully")
        }
    )
    public ResponseEntity<Void> addRelationCustomeWithStablishment(
            @RequestBody @Parameter(description = "User ID and establishment code") AddRelationUserWithStablishmentRequestDto requestDto,
            @RequestHeader("Authorization") String token
        ) {

        userStablishmentService.createUserCustomerStablishmentRelation(
                token,
                requestDto.getStablishmentCode()
        );
        return ResponseEntity.ok().build();
    }
    // for create employees
    @PostMapping("/add-relation-employee-stablishment")
    @Operation(
        summary = "Add relation between user and establishment",
        description = "Creates a relation between a user and an establishment.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Relation created successfully")
        }
    )
    public ResponseEntity<Void> addRelationEmployeeWithStablishment(
            @RequestBody @Parameter(description = "User ID and establishment code") AddRelationUserWithStablishmentRequestDto requestDto) {

        userStablishmentService.createUserEmployeeStablishmentRelation(
                requestDto.getUserId(),
                requestDto.getStablishmentCode()
        );
        return ResponseEntity.ok().build();
    }

    // =========================================================
    // DELETE RELATION BY USER (ADMIN)
    // =========================================================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-relations-by-stablishment/{userId}")
    @Operation(
        summary = "Delete relation by user",
        description = "Deletes all relations for a user. Requires ADMIN role.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Relation deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN role required")
        }
    )
    public ResponseEntity<Void> deleteRelationsByStablishment(
            @PathVariable @Parameter(description = "ID of the user") Long userId) {

        userStablishmentService.deleteEmployeeStablishmentRelations(userId);
        return ResponseEntity.ok().build();
    }

    // =========================================================
    // GET ALL EMPLOYEES OF AN ESTABLISHMENT (ADMIN / SELLER)
    // =========================================================
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    @GetMapping("/get-all-employee-relations")
    @Operation(
        summary = "Get all employees of an establishment",
        description = "Retrieve all employees related to the establishment associated with the token. Requires ADMIN or SELLER role.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Employees retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN or SELLER role required")
        }
    )
    public ResponseEntity<List<UserStablishmentResponseDto>> getAllEmployees(
            @RequestHeader("Authorization") @Parameter(description = "Bearer token") String authHeader) {

        return ResponseEntity.ok(
                userStablishmentService.getAllEmployess(authHeader)
        );
    }

    // =========================================================
    // CREATE CUSTOMER ↔ ESTABLISHMENT RELATION (CUSTOMER)
    // =========================================================
  
    // =========================================================
    // DELETE CUSTOMER ↔ ESTABLISHMENT RELATION (CUSTOMER)
    // =========================================================
    @PreAuthorize("hasRole('SERVICE')")
    @DeleteMapping("/delete-customer-relation")
    @Operation(
        summary = "Delete customer relation",
        description = "Deletes the relation of the current customer with its establishment. Requires SERVICE role.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Customer relation deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - SERVICE role required")
        }
    )
    public ResponseEntity<Void> deleteCustomerRelation(
            @RequestHeader("Authorization") @Parameter(description = "Bearer token") String authHeader) {

        userStablishmentService.deleteCustomerRelation(authHeader);
        return ResponseEntity.ok().build();
    }

    // =========================================================
    // DELETE CUSTOMER ↔ ESTABLISHMENT RELATION BY CODE (CUSTOMER)
    // =========================================================
    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/delete-customer-relation-by-code/{stablishmentCode}")
    @Operation(
        summary = "Delete customer relation by establishment code",
        description = "Deletes the relation of the current customer with a specific establishment by code. Requires CUSTOMER role.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Customer relation deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - CUSTOMER role required"),
            @ApiResponse(responseCode = "404", description = "Establishment not found")
        }
    )
    public ResponseEntity<Void> deleteCustomerRelation(
            @RequestHeader("Authorization") @Parameter(description = "Bearer token") String authHeader,
            @PathVariable @Parameter(description = "Code of the establishment") String stablishmentCode) {

        userStablishmentService.deleteCustomerRelationByCodeAndUserId(authHeader, stablishmentCode);
        return ResponseEntity.ok().build();
    }
}

