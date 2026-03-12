package com.transactionservice.transactionservice.v1.dto.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRedeemTransactionRequestDto {

    // ===== INCENTIVE / PRODUCT =====
    @NotNull
    private Long incentiveId;

    @NotNull
    private Long productId;

    @NotNull
    private String stablishmentCode;

}
