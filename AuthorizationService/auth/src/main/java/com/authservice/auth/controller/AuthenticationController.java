package com.authservice.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authservice.auth.dto.auth.AuthLoginCustomerRequestDto;
import com.authservice.auth.dto.auth.AuthLoginEmployeeRequestDto;
import com.authservice.auth.dto.auth.AuthRegisterCustomerRequestDto;
import com.authservice.auth.dto.auth.AuthRegisterEmployeeRequestDTO;
import com.authservice.auth.dto.auth.AuthResponseDto;
import com.authservice.auth.dto.auth.AuthUpdateCustomerRequestDto;
import com.authservice.auth.jwt.JwtUtil;
import com.authservice.auth.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
    name = "Authentication Controller",
    description = "Endpoints for user authentication and registration"
)
public class AuthenticationController {

    private final JwtUtil jwtUtil;

    // TODO: IMPLEMENT open api SPECIFICATION 👍
    // Upgrade springdoc especifications for more information for the user👍
    // implement validations 👍
    // activate h2 console 👍
    private final AuthenticationService authenticationService;



    
    // Customer Endpoints
    @PostMapping("/register-customer")
    @Operation(
        summary = "Register a new customer",
        description = "Endpoint to register a new customer in the system.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Customer registered successfully"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid input data"
            )
        }
    )
    public ResponseEntity<AuthResponseDto> registerCustomer(@RequestBody @Valid AuthRegisterCustomerRequestDto request) {
        authenticationService.registerCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login-customer")
    @Operation(
        summary = "Customer login",
        description = "Endpoint for customers to log in to the system.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Customer logged in successfully"
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized - Invalid credentials"
            )
        }
    )
    public ResponseEntity<AuthResponseDto> loginCustomer(@RequestBody @Valid AuthLoginCustomerRequestDto entity) {
        return ResponseEntity.ok(authenticationService.loginCustomer(entity));
    }

    @PutMapping("/update-customer/{id}")
    @Operation(

        summary = "Update an existing customer",
        description = "Endpoint to update an existing customer's details.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Customer updated successfully"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid input data"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Customer not found"
            )
        }
    )
    public ResponseEntity<Void> updateCustomer(@PathVariable Long id, @RequestBody @Valid AuthUpdateCustomerRequestDto entity) {
        authenticationService.updateCustomer(id, entity);
        
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/delete-customer")
    @Operation(
        summary = "Delete an existing customer",
        description = "Endpoint to delete an existing customer from the system.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Customer deleted successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Customer not found"
            )
        }
    )
    public ResponseEntity<Void> deleteCustomer() {
        authenticationService.deleteCustomer();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
     // End customer Endpoints
     // Employee Endpoints
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register-employee")
    // Only admin can register employee
    @Operation(
        summary = "Register a new employee",
        description = "Endpoint to register a new employee in the system. Only accessible by admins.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Employee registered successfully"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid input data"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - Admin access required"
            )
        }
    )
    public ResponseEntity<Void> registerSeller(@RequestBody @Valid AuthRegisterEmployeeRequestDTO request) {
        authenticationService.registerEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-employee/{id}")
    @Operation(

        summary = "Update an existing employee",
        description = "Endpoint to update an existing employee's details. Only accessible by admnins.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Employee updated successfully"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid input data"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - Admin access required"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Employee not found"
            )
        }
    )
    public ResponseEntity<Void> updateEmployee(@PathVariable Long id, @RequestBody @Valid AuthUpdateCustomerRequestDto entity) {
        authenticationService.updateEmployee(id, entity);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/login-employee") // Seller and Admin login
    @Operation(
        summary = "Employee login",
        description = "Endpoint for employees (sellers and admins) to log in to the system.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Employee logged in successfully"
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized - Invalid credentials"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Employee not found"
            )
        }
    )
    public ResponseEntity<AuthResponseDto> postMethodName(@RequestBody @Valid AuthLoginEmployeeRequestDto request) {
        return ResponseEntity.ok(authenticationService.loginEmployee(request));
    }
    
    // End Employee Endpoints
    // Admin Endpoints
    @PostMapping("/create-establishment-admin") // Is used to create the first admin of an establishment, called by the establishment service
    @Operation(
        summary = "Create establishment admin",
        description = "Endpoint to create the first admin of an establishment, called by the establishment service.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Establishment admin created successfully"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid input data"
            )
        }
    )
    public ResponseEntity<Void> createEstablishmentAdmin(@RequestBody @Valid AuthRegisterEmployeeRequestDTO entity) {        
        authenticationService.createEstablishmentAdmin(entity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("/delete-employee/{id}")
    @Operation(
        summary = "Delete an existing employee",
        description = "Endpoint to delete an existing employee from the system. Only accessible by admins.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Employee deleted successfully"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - Admin access required"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Employee not found"
            )
        }
    )
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        authenticationService.deleteEmployee(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping("/test")
    public String postMethodName(@RequestBody String entity) {
        
        return jwtUtil.generateServiceToken("auth-service");
    }
    
    
    
    
    
}
