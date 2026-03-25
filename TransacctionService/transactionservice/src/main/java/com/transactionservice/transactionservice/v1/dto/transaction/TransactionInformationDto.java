package com.transactionservice.transactionservice.v1.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class TransactionInformationDto {
    private int redeemedProducts;
    private int pointsAwarded;
}
