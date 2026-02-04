package com.incentiveservice.incentiveservice.v1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incentiveservice.incentiveservice.v1.dto.userpoints.UserPointsCreateRequestDto;
import com.incentiveservice.incentiveservice.v1.dto.userpoints.UserPointsDto;
import com.incentiveservice.incentiveservice.v1.dto.userpoints.UserPointsUpdateRequestDto;
import com.incentiveservice.incentiveservice.v1.dto.userpoints.UsersPointsDto;
import com.incentiveservice.incentiveservice.v1.service.userpoints.UserPointsService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/v1/user-points")
@RequiredArgsConstructor
public class UserPointsController {

    private final UserPointsService userPointsService;
    // =========================================================    
    // CREATE USER POINTS
    // =========================================================
    @Operation(
        summary = "Create user points wallet",
        description = "Creates a points wallet for a user and a stablishment. Intended for internal SERVICE usage.",
        responses = {
            @ApiResponse(responseCode = "201", description = "User points wallet created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Wallet already exists")
        }
    )
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "User points creation data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = UserPointsCreateRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "Create user points",
                    value = """
                    {
                    "userId": 25,
                    "stablishmentCode": "EST-9F3A2B"
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Void> createUserPoints(
            @RequestBody UserPointsCreateRequestDto request
    ) {

        userPointsService.create(request.getUserId(), request.getStablishmentCode());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // =========================================================
    // GET USER POINTS
    // =========================================================
    @Operation(
        summary = "Get user points",
        description = "Returns the points of a user for a specific stablishment.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User points retrieved successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserPointsDto.class),
                    examples = {
                        @ExampleObject(
                            name = "User points response",
                            value = """
                            {
                            "userId": 25,
                            "stablishmentCode": "EST-9F3A2B",
                            "points": 320
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(responseCode = "404", description = "User points not found")
        }
    )
    @PreAuthorize("hasRole('SERVICE') or hasAutority('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{stablishmentCode}")
    public ResponseEntity<UserPointsDto> getUserPoints(
            @RequestHeader("Authorization") String token,
            @PathVariable
            @Parameter(description = "Stablishment code", example = "SAZ-0001")
            String stablishmentCode
    ) {

        return ResponseEntity.ok(
                userPointsService.getUserPointsByUserIdAndStablishmentCode(token, stablishmentCode)
        );
    }

    // =========================================================
    // SUBSTRAC USER POINTS
    // =========================================================
    @Hidden
    @Operation(
        summary = "Subtract user points",
        description = "Subtracts points from a user's wallet.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Points subtracted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid amount"),
            @ApiResponse(responseCode = "404", description = "User points not found")
        }
    )
    @PreAuthorize("hasRole('SERVICE')")
    @PutMapping("/rest")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Points subtraction data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = UserPointsUpdateRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "Subtract points",
                    value = """
                    {
                    "userId": 25,
                    "stablishmentCode": "EST-9F3A2B",
                    "amount": 50
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Void> subtractPoints(
            @RequestBody UserPointsUpdateRequestDto request
    ) {

        userPointsService.subtractPoints(
                request.getUserId(),
                request.getStablishmentCode(),
                request.getAmount()
        );

        return ResponseEntity.ok().build();
    }

    // =========================================================
    // ADD USER POINTS
    // =========================================================
    @Operation(
        summary = "Add user points",
        description = "Adds points to a user's wallet.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Points added successfully"),
            @ApiResponse(responseCode = "404", description = "User points not found")
        }
    )
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @PutMapping("/sum")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Points addition data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = UserPointsUpdateRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "Add points",
                    value = """
                    {
                    "userId": 25,
                    "stablishmentCode": "EST-9F3A2B",
                    "amount": 100
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Void> addPoints(
            @RequestBody UserPointsUpdateRequestDto request
    ) {

        userPointsService.addPoints(
                request.getUserId(),
                request.getStablishmentCode(),
                request.getAmount()
        );

        return ResponseEntity.ok().build();
    }

    // =========================================================
    // DELETE USER POINTS
    // =========================================================
    @Operation(
        summary = "Delete user points wallet",
        description = "Deletes a user's points wallet for a specific stablishment.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "User points deleted successfully",
                content = @Content(
                    schema = @Schema(implementation = UsersPointsDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Deleted user points",
                            value = """
                            {
                            "users": [
                                {
                                "userId": 25,
                                "stablishmentCode": "EST-9F3A2B",
                                "points": 320
                                }
                            ]
                            }
                            """
                        )
                    }
                )
            )
        }
    )
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @DeleteMapping("/{userId}/{stablishmentCode}/delete")
    public ResponseEntity<UsersPointsDto> deleteUserPoints(
            @PathVariable Long userId,
            @PathVariable String stablishmentCode
    ) {

        return ResponseEntity.ok(
                userPointsService.deleteUserPoints(userId, stablishmentCode)
        );
    }

    // =========================================================
    // DELETE ALL USER POINTS BY AN USER
    // =========================================================
    @Operation(
        summary = "Delete all user wallets",
        description = "Deletes all points wallets for a user (internal SERVICE usage).",
        responses = {
            @ApiResponse(responseCode = "200", description = "User wallets deleted successfully")
        }
    )
    @PreAuthorize("hasRole('SERVICE')")
    @DeleteMapping("/delete-all-user-wallets/{userId}")
    public ResponseEntity<UsersPointsDto> deleteAllUserPoints(
            @PathVariable
            @Parameter(description = "User ID", example = "25")
            Long userId
    ) {
        System.out.println("entro"+" "+userId);
        return ResponseEntity.ok(
                userPointsService.deleteAllUserPointsByUserId(userId)
        );
    }
    // =========================================================
    // DELETE ALL USER POINTS BY STABLISHMENT 
    // =========================================================
    @Operation(
        summary = "Delete all user wallets by stablishment",
        description = "Deletes all user points wallets associated with a stablishment.",
        responses = {
            @ApiResponse(responseCode = "200", description = "User wallets deleted successfully")
        }
    )
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @DeleteMapping("/delete-all-users-wallets-by-stablishment-code/{stablishmentCode}")
    public ResponseEntity<UsersPointsDto> deleteAllUserPoints(
            @PathVariable
            @Parameter(description = "Stablishment code", example = "EST-9F3A2B")
            String stablishmentCode
    ) {

        return ResponseEntity.ok(
                userPointsService.deleteAllUserPointsByStablishmentCode(stablishmentCode)
        );
    }

    // =========================================================
    // ROLLBACK DELETED USER POINTS
    // =========================================================
    @Operation(
        summary = "Rollback deleted user points",
        description = "Restores previously deleted user points wallets (saga rollback).",
        responses = {
            @ApiResponse(responseCode = "200", description = "User points restored successfully")
        }
    )
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping("/rollback-delete")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Rollback user points data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = UsersPointsDto.class),
            examples = {
                @ExampleObject(
                    name = "Rollback user points",
                    value = """
                    {
                    "users": [
                        {
                        "userId": 25,
                        "stablishmentCode": "EST-9F3A2B",
                        "points": 320
                        }
                    ]
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Void> rollbackDelete(
            @RequestBody UsersPointsDto request
    ) {

        userPointsService.createAll(request);
        return ResponseEntity.ok().build();
    }

    
}
