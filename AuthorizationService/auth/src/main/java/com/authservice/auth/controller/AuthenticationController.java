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
import com.authservice.auth.dto.auth.AuthUpdateEmployeeRequestDto;
import com.authservice.auth.dto.auth.ChangePassWordDto;
import com.authservice.auth.dto.auth.UsersDto;
import com.authservice.auth.dto.stablisment.RollbackDeleteEmployeesRequestDto;
import com.authservice.auth.dto.stablisment.UsersIdsRequestDto;
import com.authservice.auth.dto.stablisment.UsersQuantityResponseDto;
import com.authservice.auth.service.authentication.AuthenticationCustomerService;
import com.authservice.auth.service.authentication.AuthenticationEmployeeService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
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
    
    // =========================================================
    // REGISTER CUSTOMER
    // =========================================================
    @PostMapping("/customers")
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
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Customer registration data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = AuthRegisterCustomerRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "Valid customer",
                    summary = "Example of a valid customer registration",
                    value = """
                    {
                        "username": "622926844",
                        "name": "Kevin",
                        "lastname": "Zelaya",
                        "password": "Password123"
                    }
                    """
                )
            }
        )
    )
    @PermitAll
    public ResponseEntity<AuthResponseDto> registerCustomer(
        @RequestBody @Valid AuthRegisterCustomerRequestDto request
    ) {
        authenticationCustomerService.registerCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    // =========================================================
    // LOGIN CUSTOMER
    // =========================================================
    @PostMapping("/login/customers")
    @Operation(
        summary = "Customer login",
        description = "Endpoint for customers to log in to the system.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Customer logged in successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponseDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Login success",
                            summary = "JWT token response",
                            value = """
                            {
                            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                            "expiresIn": 3600
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized - Invalid credentials"
            )
        }
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Login customer data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = AuthLoginCustomerRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "Valid customer",
                    summary = "Example of a valid customer loggin",
                    value = """
                    {
                    "username": "622926844",
                    "password": "Password123"
                    }
                    """
                )
            }
        )
    )
    @PermitAll
    public ResponseEntity<AuthResponseDto> loginCustomer(
        @RequestBody @Valid AuthLoginCustomerRequestDto entity
    ) {
        return ResponseEntity.ok(authenticationCustomerService.loginCustomer(entity));
    }
    // =========================================================
    // UPDATE CUSTOMER
    // =========================================================
    @PutMapping("/customers")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
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
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Customer update data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = AuthUpdateCustomerRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "Valid customer",
                    summary = "Example of a valid customer update",
                    value = """
                    {
                        "username": "688965423",
                        "name": "Misahel",
                        "lastname": "Peña",
                        "password": "password321"
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Void> updateCustomer(
        @RequestHeader("Authorization") String token, 
        @RequestBody @Valid AuthUpdateCustomerRequestDto entity) {
        authenticationCustomerService.updateCustomer(token, entity);
        
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // =========================================================
    // DELETE CUSTOMER
    // =========================================================
    // TODO: al eliminar si no hay relaciones da error
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/customers")
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
    // =========================================================
    // EGISTER EMPLOYEE
    // =========================================================
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/employees")
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
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Employee create data for admin",
        required = true,
        content = @Content(
            schema = @Schema(implementation = AuthRegisterEmployeeRequestDTO.class),
            examples = {
                @ExampleObject(
                    name = "Valid Employee",
                    summary = "Example of a valid employee update",
                    value = """
                    {
                    "username": "+34678123456",
                    "password": "Password123",
                    "name": "Juan",
                    "lastname": "Pérez",
                    "role": "SELLER"
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Void> registerSeller(
        @RequestBody @Valid AuthRegisterEmployeeRequestDTO request
    ) {
        authenticationEmployeeService.registerEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // =========================================================
    // UPDATE EMPLOYEE
    // =========================================================
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/employees/{id}")
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
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Employee update data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = AuthUpdateEmployeeRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "Valid Employee",
                    summary = "Example of a valid employee update",
                    value = """
                    {
                        "username": "688965423",
                        "name": "Misahel",
                        "lastname": "Peña",
                        "password": "password321"
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Void> updateEmployee(
        @PathVariable Long id, 
        @RequestBody @Valid AuthUpdateCustomerRequestDto entity
    ) {
        authenticationEmployeeService.updateEmployee(id, entity);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // =========================================================
    // LOGIN EMPLOYEE
    // =========================================================
    @PostMapping("/login/employees") // Seller and Admin login
    @PermitAll
    @Operation(
        summary = "Employee login",
        description = "Endpoint for employees (sellers and admins) to log in to the system.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Employee logged in successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponseDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Login success",
                            summary = "JWT token response",
                            value = """
                            {
                            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                            "expiresIn": 3600
                            }
                            """
                        )
                    }
                )
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
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Login employee data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = AuthLoginEmployeeRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "Valid employee",
                    summary = "Example of a valid employee loggin",
                    value = """
                    {
                        "username": "666889922",
                        "password": "Password3@",
                        "establishmentCode": "SAZ-0001"
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<AuthResponseDto> loginEmployee(
        @RequestBody @Valid AuthLoginEmployeeRequestDto request
    ) {
        return ResponseEntity.ok(authenticationEmployeeService.loginEmployee(request));
    }
    // =========================================================
    // CREATE STABLISHMENT ADMIN
    // =========================================================
    // Admin Endpoints
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping("/create/establishment-admin") // Is used to create the first admin of an establishment, called by the establishment service
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
    // =========================================================
    // DELETE AN EMPLOYEE
    // =========================================================
    @PreAuthorize("hasAuthority('ADMIN') or hasRole('SERVICE')")
    @DeleteMapping("/employees/{id}")
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

    // =========================================================
    // DELETE ALL EMPLOYEES
    // =========================================================
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping("/delete/employees")
    @Operation(
        summary = "Delete multiple employees",
        description = "Endpoint to delete multiple employees from the system. Only accessible by service role.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Employees deleted successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = UsersIdsRequestDto.class
                    ),
                    examples = {
                        @ExampleObject(
                            name = "Successful deletion",
                            summary = "Employees deleted correctly",
                            value = """
                            {
                            "deletedUsers": [
                                {
                                "id": 10,
                                "username": "689654569",
                                "name": "Kevin",
                                "lastname": "Zelaya"
                                "role": "ADMIN"
                                },
                                {
                                "id": 11,
                                "username": "689654565",
                                "name": "Kevin",
                                "lastname": "Peña",
                                "role": "SELLER"
                                }
                            ]
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - Service access required"
            )
        }
    )
    public ResponseEntity<UsersDto> deleteEmployees(
        @RequestBody UsersIdsRequestDto request
    ) {
        UsersDto response =
            authenticationEmployeeService.deleteEmployees(request);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // ROLLBACK DELETE EMPLOYEES
    // =========================================================
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping("/employees/rollback-delete")
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
    // GET METHODS
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping("/employees/search")
    public ResponseEntity<UsersDto> getEmployees(
        @RequestBody UsersIdsRequestDto request
    ) {
        UsersDto users = authenticationEmployeeService.getAllUsers(request);
        users.getUsers().stream().forEach(user -> System.out.println(user.getUsername()));
        return ResponseEntity.ok(users);
    }
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping("/customers/search")
    public ResponseEntity<UsersDto> getCustomers(
        @RequestBody UsersIdsRequestDto request
    ) {
        UsersDto users = authenticationCustomerService.getAllUsers(request);
        users.getUsers().stream().forEach(user -> System.out.println(user.getUsername()));
        return ResponseEntity.ok(users);
    }
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping("/dashboard/search")
    public ResponseEntity<UsersQuantityResponseDto> getUsersQuantity(
        @RequestBody UsersIdsRequestDto request
    ) {
        UsersQuantityResponseDto response = authenticationEmployeeService.getUsersQuantity(request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
    summary = "Change employee password",
    description = "Allows ADMIN to change an employee password.",
    responses = {
        @ApiResponse(responseCode = "200", description = "Password updated successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "User not found")
    }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/employees/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePassWordDto dto
    ) {
        authenticationEmployeeService.changePassword(id, dto.getNewPassword());
        return ResponseEntity.ok().build();
    }
    
}
