package com.transactionservice.transactionservice.v1.dto.transactionprocess;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionPointsDto {
    private String stablishmentCode;
    private Long customerId;
    private BigDecimal amountSpent;
}
