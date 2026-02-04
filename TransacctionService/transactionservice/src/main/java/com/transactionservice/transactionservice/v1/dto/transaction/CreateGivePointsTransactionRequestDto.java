package com.transactionservice.transactionservice.v1.dto.transaction;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGivePointsTransactionRequestDto {

    @NotNull
    @Min(1)
    private BigDecimal amountSpent;
}
