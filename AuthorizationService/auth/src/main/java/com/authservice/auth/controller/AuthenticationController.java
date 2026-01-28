package com.authservice.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authservice.auth.dto.auth.AuthCreateEstablishMentAdminResponseDto;
import com.authservice.auth.dto.auth.AuthLoginCustomerRequestDto;
import com.authservice.auth.dto.auth.AuthLoginEmployeeRequestDto;
import com.authservice.auth.dto.auth.AuthRegisterCustomerRequestDto;
import com.authservice.auth.dto.auth.AuthRegisterEmployeeRequestDTO;
import com.authservice.auth.dto.auth.AuthResponseDto;
import com.authservice.auth.dto.auth.AuthUpdateCustomerRequestDto;
import com.authservice.auth.dto.stablisment.DeleteUsersInAuthServiceRequestDto;
import com.authservice.auth.dto.stablisment.DeleteUsersInAuthServiceResponseDto;
import com.authservice.auth.dto.stablisment.RollbackDeleteEmployeesRequestDto;
import com.authservice.auth.jwt.JwtUtil;
import com.authservice.auth.service.authentication.AuthenticationCustomerService;
import com.authservice.auth.service.authentication.AuthenticationEmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;




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
    private final AuthenticationEmployeeService authenticationEmployeeService;
    private final AuthenticationCustomerService authenticationCustomerService;



    
    // Customer Endpoints
    @GetMapping("/get-user-id-by-username/{username}")
    public ResponseEntity<Long> postMethodName(
        @PathVariable String username
    ) {
        Long id = authenticationCustomerService.getCustomerIdByUsername(username);
        return ResponseEntity.ok(id);
    }
    
    

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
    public ResponseEntity<AuthResponseDto> registerCustomer(
        @RequestBody @Valid AuthRegisterCustomerRequestDto request
    ) {
        authenticationCustomerService.registerCustomer(request);
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
    public ResponseEntity<AuthResponseDto> loginCustomer(
        @RequestBody @Valid AuthLoginCustomerRequestDto entity
    ) {
        return ResponseEntity.ok(authenticationCustomerService.loginCustomer(entity));
    }

    @PutMapping("/update-customer")
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
    public ResponseEntity<Void> updateCustomer(
        @RequestHeader("Authorization") String token, 
        @RequestBody @Valid AuthUpdateCustomerRequestDto entity) {
        authenticationCustomerService.updateCustomer(token, entity);
        
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
        authenticationCustomerService.deleteCustomer();
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
    public ResponseEntity<Void> registerSeller(
        @RequestBody @Valid AuthRegisterEmployeeRequestDTO request
    ) {
        authenticationEmployeeService.registerEmployee(request);
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
        authenticationEmployeeService.updateEmployee(id, entity);
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
    public ResponseEntity<AuthResponseDto> loginEmployee(
        @RequestBody @Valid AuthLoginEmployeeRequestDto request
    ) {
        return ResponseEntity.ok(authenticationEmployeeService.loginEmployee(request));
    }
    
    // End Employee Endpoints
    // Admin Endpoints
    @PreAuthorize("hasRole('SERVICE')")
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
    public ResponseEntity<AuthCreateEstablishMentAdminResponseDto> createEstablishmentAdmin(
        @RequestBody @Valid AuthRegisterEmployeeRequestDTO entity
    ) {        
        AuthCreateEstablishMentAdminResponseDto response = authenticationEmployeeService.createEstablishmentAdmin(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('SERVICE')")
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
    public ResponseEntity<Void> deleteEmployee(
        @PathVariable Long id
    ) {
        authenticationEmployeeService.deleteEmployee(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping("/delete-employees")
     @Operation(
        summary = "Delete multiple employees",
        description = "Endpoint to delete multiple employees from the system. Only accessible by service role.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Employees deleted successfully"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - Service access required"
            )
        }
    )
    public ResponseEntity<DeleteUsersInAuthServiceResponseDto> deleteEmployees(
        @RequestBody DeleteUsersInAuthServiceRequestDto request
    ) {
        DeleteUsersInAuthServiceResponseDto response = authenticationEmployeeService.deleteEmployees(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping("/rollback-delete-employees")
     @Operation(
        summary = "Rollback delete employees",
        description = "Endpoint to rollback the deletion of multiple employees in the system. Only accessible by service role.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Employees rollbacked successfully"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - Service access required"
            )
        }
    )
    public ResponseEntity<Void> rollbackDeleteEmployees(
        @RequestBody RollbackDeleteEmployeesRequestDto request
    ) {
        authenticationEmployeeService.rollbackDeleteEmployees(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    
}
