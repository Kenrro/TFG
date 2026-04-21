package com.stablishmentservice.stablishmentservice.dto.stablishment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class TransactionInformationDto {
    private Integer transactionsQuantity;
    private int pointsAwarded;
}
