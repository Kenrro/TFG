package com.stablishmentservice.stablishmentservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stablishmentservice.stablishmentservice.dto.authorization.AddRelationCustomerWithStablishmentRequestDto;
import com.stablishmentservice.stablishmentservice.dto.authorization.AddRelationUserWithStablishmentRequestDto;
import com.stablishmentservice.stablishmentservice.dto.authorization.UsersDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.UserStablishmentResponseDto;
import com.stablishmentservice.stablishmentservice.service.userStablishment.UserStablishmentService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

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
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/add-relation-customer-stablishment")
    @Operation(
        summary = "Add relation between user and establishment",
        description = "Creates a relation between the authenticated user and an establishment.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Relation created successfully"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid request data"
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized - invalid or missing token"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - CUSTOMER role required"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Establishment not found"
            )
        }
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "User and establishment relation data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = AddRelationCustomerWithStablishmentRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "Create relation example",
                    summary = "Valid relation request",
                    value = """
                    {
                        "stablishmentCode": "SAZ-0001"
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Void> addRelationCustomeWithStablishment(
            @Valid @RequestBody AddRelationCustomerWithStablishmentRequestDto requestDto,
            @RequestHeader("Authorization") String token
    ) {

        userStablishmentService.createUserCustomerStablishmentRelation(
                token,
                requestDto.getStablishmentCode()
        );

        return ResponseEntity.ok().build();
    }

    // =========================================================
    // CREATE EMPLOYEE ↔ ESTABLISHMENT RELATION
    // =========================================================
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/add-relation-employee-stablishment")
    @Operation(
        summary = "Add relation between employee and establishment",
        description = "Creates a relation between an employee and an establishment.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Relation created successfully"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid request data"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User or establishment not found"
            )
        }
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Employee and establishment relation data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = AddRelationUserWithStablishmentRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "Create employee relation",
                    summary = "Valid employee relation request",
                    value = """
                    {
                    "userId": 45,
                    "stablishmentCode": "EST-9F3A2B"
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Void> addRelationEmployeeWithStablishment(
            @Valid @RequestBody AddRelationUserWithStablishmentRequestDto requestDto
    ) {

        userStablishmentService.createUserEmployeeStablishmentRelation(
                requestDto.getUserId(),
                requestDto.getStablishmentCode()
        );

        return ResponseEntity.ok().build();
    }


    // =========================================================
    // DELETE RELATIONS BY USER (ADMIN)
    // =========================================================
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete-relations-by-stablishment/{userId}")
    @Operation(
        summary = "Delete relations by user",
        description = "Deletes all establishment relations for a user. Requires ADMIN role.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Relations deleted successfully"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - ADMIN role required"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found"
            )
        }
    )
    public ResponseEntity<Void> deleteRelationsByStablishment(
            @PathVariable
            @Parameter(description = "ID of the user whose relations will be deleted", example = "15")
            Long userId
    ) {

        userStablishmentService.deleteEmployeeStablishmentRelations(userId);
        return ResponseEntity.ok().build();
    }


    // =========================================================
    // GET ALL EMPLOYEES OF AN ESTABLISHMENT (ADMIN / SELLER)
    // =========================================================
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SELLER')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/get-all-employee-relations")
    @Operation(
        summary = "Get all employees of an establishment",
        description = "Retrieve all employees related to the establishment associated with the token. Requires ADMIN or SELLER role.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Employees retrieved successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserStablishmentResponseDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Employees list",
                            summary = "Example response",
                            value = """
                            [
                            {
                                "userId": 12,
                                "username": "688965423",
                                "name": "Laura",
                                "lastname": "García",
                                "role": "EMPLOYEE"
                            },
                            {
                                "userId": 18,
                                "username": "699887766",
                                "name": "Carlos",
                                "lastname": "Pérez",
                                "role": "EMPLOYEE"
                            }
                            ]
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - ADMIN or SELLER role required"
            )
        }
    )
    public ResponseEntity<UsersDto> getAllEmployees(
            @RequestHeader("Authorization")
            @Parameter(description = "Bearer token")
            String authHeader
    ) {

        return ResponseEntity.ok(
                userStablishmentService.getAllEmployess(authHeader)
        );
    }

    // =========================================================
    // DELETE ALL CUSTOMER RELATIONS
    // =========================================================
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @DeleteMapping("/delete-user-relations/{userId}")
    @Operation(
        summary = "Delete customer relation",
        description = "Deletes the customer–establishment relation for the given user. Requires SERVICE role.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Customer relation deleted successfully"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - SERVICE role required"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User or relation not found"
            )
        }
    )
    public ResponseEntity<Void> deleteCustomerRelation(
            @PathVariable
            @Parameter(description = "ID of the customer whose relation will be deleted", example = "22")
            Long userId
    ) {

        userStablishmentService.deleteCustomerRelationByUserId(userId);
        return ResponseEntity.ok().build();
    }

    // =========================================================
    // DELETE CUSTOMER ↔ ESTABLISHMENT RELATION BY CODE (CUSTOMER)
    // =========================================================
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @DeleteMapping("/delete-customer-relation-by-code/{stablishmentCode}")
    @Operation(
        summary = "Delete customer relation by establishment code",
        description = "Deletes the relation of the authenticated customer with a specific establishment using its code. Requires CUSTOMER role.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Customer relation deleted successfully"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - CUSTOMER role required"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Establishment or relation not found"
            )
        }
    )
    public ResponseEntity<Void> deleteCustomerRelation(
            @RequestHeader("Authorization")
            @Parameter(description = "Bearer token for authentication")
            String authHeader,

            @PathVariable
            @Parameter(description = "Code of the establishment", example = "EST-9F3A2B")
            String stablishmentCode
    ) {

        userStablishmentService
                .deleteCustomerRelationByCodeAndUserId(authHeader, stablishmentCode);

        return ResponseEntity.ok().build();
    }
}

