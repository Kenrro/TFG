package com.authservice.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authservice.auth.dto.auth.AuthLoginCustomerRequestDto;
import com.authservice.auth.dto.auth.AuthLoginEmployeeRequestDto;
import com.authservice.auth.dto.auth.AuthRegisterCustomerRequestDto;
import com.authservice.auth.dto.auth.AuthRegisterEmployeeRequestDTO;
import com.authservice.auth.dto.auth.AuthResponseDto;
import com.authservice.auth.dto.auth.AuthUpdateCustomerRequestDto;
import com.authservice.auth.service.AuthenticationService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    // TODO: IMPLEMENT open api SPECIFICATION 👍
    // Upgrade springdoc especifications for more information for the user👍
    // implement validations 👍
    // activate h2 console 👍
    private final AuthenticationService authenticationService;
    // Customer Endpoints
    @PostMapping("/register-customer")
    public ResponseEntity<AuthResponseDto> registerCustomer(@RequestBody @Valid AuthRegisterCustomerRequestDto request) {
        return ResponseEntity.ok(authenticationService.registerCustomer(request));
    }
    @PostMapping("/login-customer")
    public ResponseEntity<AuthResponseDto> loginCustomer(@RequestBody @Valid AuthLoginCustomerRequestDto entity) {
        return ResponseEntity.ok(authenticationService.loginCustomer(entity));
    }
    
     // End customer Endpoints
     // Employee Endpoints
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register-employee") // Only admin can register employee
    public ResponseEntity<Void> registerSeller(@RequestBody @Valid AuthRegisterEmployeeRequestDTO request) {
        authenticationService.registerEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-employee/{id}")
    public ResponseEntity<Void> putMethodName(@PathVariable Long id, @RequestBody @Valid AuthUpdateCustomerRequestDto entity) {
        authenticationService.updateEmployee(id, entity);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping("/login-employee") // Seller and Admin login
    public ResponseEntity<AuthResponseDto> postMethodName(@RequestBody @Valid AuthLoginEmployeeRequestDto request) {
        return ResponseEntity.ok(authenticationService.loginEmployee(request));
    }
    
    // End Employee Endpoints
    // Admin Endpoints
    @PostMapping("/create-establishment-admin") // Is used to create the first admin of an establishment, called by the establishment service
    public ResponseEntity<Void> postMethodName(@RequestBody @Valid AuthRegisterEmployeeRequestDTO entity) {        
        authenticationService.createEstablishmentAdmin(entity);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    
    
    
}
