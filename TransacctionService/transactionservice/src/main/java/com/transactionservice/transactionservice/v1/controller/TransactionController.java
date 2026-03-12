package com.transactionservice.transactionservice.v1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transactionservice.transactionservice.v1.dto.transaction.CreateGivePointsTransactionRequestDto;
import com.transactionservice.transactionservice.v1.dto.transaction.CreateRedeemTransactionRequestDto;
import com.transactionservice.transactionservice.v1.dto.transaction.GivePointsTransactionsResponseDto;
import com.transactionservice.transactionservice.v1.dto.transaction.RedeemProductTransactionsResponseDto;
import com.transactionservice.transactionservice.v1.dto.transaction.TransactionUUIDDto;
import com.transactionservice.transactionservice.v1.service.transaction.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    // =========================================================
    // CREATE GIVE POINTS TRANSACTION
    // =========================================================
    @Operation(
        summary = "Create give points transaction",
        description = "Creates a transaction to give points to a customer. Requires ADMIN role.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Transaction created successfully",
                content = @Content(
                    schema = @Schema(implementation = TransactionUUIDDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Transaction UUID",
                            value = """
                            { "id": "c3c2c6a1-5b6e-4d3e-9b87-8e5c8a2f3d91" }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN role required")
        }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/give-points")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Give points transaction data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = CreateGivePointsTransactionRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "Create give points transaction",
                    value = """
                    {
                    "amountSpent": 45.90
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<TransactionUUIDDto> createGivePointsTransaction(
            @RequestBody CreateGivePointsTransactionRequestDto requestDto,
            @RequestHeader("Authorization") String token
    ) {
        TransactionUUIDDto uuid =
                transactionService.createGivePointsTransaction(token, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(uuid);
    }
    // =========================================================
    // CREATE REDEEM TRANSACTION
    // =========================================================
    @Operation(
        summary = "Create redeem transaction",
        description = "Creates a redeem transaction requested by a customer.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Redeem transaction created successfully",
                content = @Content(
                    schema = @Schema(implementation = TransactionUUIDDto.class),
                    examples = {
                        @ExampleObject(
                            value = """
                            { "id": "9f42b8e2-0f6a-4a9a-9bcb-2f6f51b2d781" }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Forbidden - CUSTOMER role required")
        }
    )
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/redeems")
    public ResponseEntity<TransactionUUIDDto> createRedeemPointsTransaction(
            @RequestBody CreateRedeemTransactionRequestDto requestDto,
            @RequestHeader("Authorization") String token
    ) {

        TransactionUUIDDto uuid =
                transactionService.createRedeemTransaction(requestDto, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(uuid);
    }
    // =========================================================
    // PROCESS GIVE POINTS TRANSACTION
    // =========================================================
    @Operation(
        summary = "Process give points transaction",
        description = "Processes a previously created give points transaction.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Transaction processed successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - CUSTOMER role required")
        }
    )
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PutMapping("/give-points")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Transaction UUID",
        required = true,
        content = @Content(
            schema = @Schema(implementation = TransactionUUIDDto.class),
            examples = {
                @ExampleObject(
                    value = """
                    { "id": "c3c2c6a1-5b6e-4d3e-9b87-8e5c8a2f3d91" }
                    """
                )
            }
        )
    )
    public ResponseEntity<Void> processGivePointsTransaction(
            @RequestBody TransactionUUIDDto request,
            @RequestHeader("Authorization") String token
    ) {

        transactionService.processGivePoints(token, request);
        return ResponseEntity.ok().build();
    }
    // =========================================================
    // CPROCESS REDEEM TRANSACTION
    // =========================================================
    @Operation(
        summary = "Process redeem transaction",
        description = "Processes a redeem transaction. Requires ADMIN role.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Redeem transaction processed successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN role required")
        }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/redeem")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Transaction UUID",
        required = true,
        content = @Content(
            schema = @Schema(implementation = TransactionUUIDDto.class),
            examples = {
                @ExampleObject(
                    value = """
                    { "id": "9f42b8e2-0f6a-4a9a-9bcb-2f6f51b2d781" }
                    """
                )
            }
        )
    )
    public ResponseEntity<Void> processRedeemTransaction(
            @RequestBody TransactionUUIDDto request,
            @RequestHeader("Authorization") String token
    ) {

        transactionService.processRedeemTransaction(token, request);
        return ResponseEntity.ok().build();
    }
    // =========================================================
    // GET GIVE POINTS TRANSACTIONS
    // =========================================================
    @Operation(
        summary = "Get give points transactions",
        description = "Returns all give points transactions for the current stablishment.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully")
        }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/points")
    public ResponseEntity<GivePointsTransactionsResponseDto> getGivePointsTransactions(
            @RequestHeader("Authorization") String token
    ) {

        return ResponseEntity.ok(
                transactionService.getGivePointsTransactions(token)
        );
    }
    // =========================================================
    // GET REDEEM TRANSACTIONS
    // =========================================================
    @Operation(
        summary = "Get redeem transactions",
        description = "Returns all redeem product transactions for the current stablishment.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully")
        }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/redeem")
    public ResponseEntity<RedeemProductTransactionsResponseDto> getRedeemTransactions(
            @RequestHeader("Authorization") String token
    ) {

        return ResponseEntity.ok(
                transactionService.getRedeemProductTransaction(token)
        );
    }

    
    
}
