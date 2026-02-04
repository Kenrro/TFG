package com.transactionservice.transactionservice.v1.dto.transaction;

import java.time.Instant;
import java.util.UUID;

import com.transactionservice.transactionservice.v1.entity.TransactionStatus;
import com.transactionservice.transactionservice.v1.entity.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RedeemProductDto {
    private UUID id;

    private TransactionType type;
    private TransactionStatus status;

    private String stablishmentCode;
    private Long employeeId;
    private Long customerId;

    private Instant createdAt;
    private Instant expiresAt;

    private Integer pointsRequired;
    private Long productId;
    private Long incentiveId;
}
