package com.incentiveservice.incentiveservice.v1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incentiveservice.incentiveservice.v1.dto.transaction.TransactionPointsDto;
import com.incentiveservice.incentiveservice.v1.dto.transaction.TransactionPointsResponseDto;
import com.incentiveservice.incentiveservice.v1.dto.transaction.TransactionRedeemDto;
import com.incentiveservice.incentiveservice.v1.service.transaction.TransactionProcessService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/v1/transaction-process")
@RequiredArgsConstructor
public class TransactionProcessController {

    private final TransactionProcessService transactionProcessService;
    // =========================================================
    // PROCESS POINTS TRANSACTIONS
    // =========================================================
    @Hidden
    @Operation(
        summary = "Process points transaction",
        description = "Processes a points transaction based on the amount spent by a customer.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Points transaction processed successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TransactionPointsResponseDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Points transaction response",
                            summary = "Example points transaction",
                            value = """
                            {
                            "customerId": 25,
                            "stablishmentCode": "EST-9F3A2B",
                            "pointsEarned": 120,
                            "totalPoints": 450
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid transaction data"),
            @ApiResponse(responseCode = "404", description = "User or stablishment not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
        }
    )
    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping("/points")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Points transaction data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = TransactionPointsDto.class),
            examples = {
                @ExampleObject(
                    name = "Process points request",
                    summary = "Valid points transaction",
                    value = """
                    {
                    "stablishmentCode": "EST-9F3A2B",
                    "customerId": 25,
                    "amountSpent": 45.90
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<TransactionPointsResponseDto> processPoints(
            @RequestBody TransactionPointsDto request
    ) {

        TransactionPointsResponseDto response =
                transactionProcessService.processPoints(request);

        return ResponseEntity.ok(response);
    }
    // =========================================================
    // REDEEM INCENTIVE
    // =========================================================
    @Operation(
        summary = "Redeem incentive",
        description = "Processes an incentive redemption transaction for a customer.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Incentive redeemed successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TransactionPointsResponseDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Redeem transaction response",
                            summary = "Example redeem transaction",
                            value = """
                            {
                            "customerId": 25,
                            "stablishmentCode": "EST-9F3A2B",
                            "pointsSpent": 150,
                            "totalPoints": 300,
                            "incentiveId": 8
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid redeem data"),
            @ApiResponse(responseCode = "404", description = "Incentive or user not found"),
            @ApiResponse(responseCode = "409", description = "Insufficient points"),
            @ApiResponse(responseCode = "500", description = "Unexpected error")
        }
    )
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping("/redeem")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Redeem transaction data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = TransactionRedeemDto.class),
            examples = {
                @ExampleObject(
                    name = "Redeem incentive request",
                    summary = "Valid redeem transaction",
                    value = """
                    {
                    "stablishmentCode": "EST-9F3A2B",
                    "customerId": 25,
                    "incentiveId": 8
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<TransactionPointsResponseDto> processRedeem(
            @RequestBody TransactionRedeemDto request
    ) {

        TransactionPointsResponseDto response =
                transactionProcessService.processRedeem(request);

        return ResponseEntity.ok(response);
    }

    
}
