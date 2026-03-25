package com.transactionservice.transactionservice.v1.dto.transaction;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IncentiveQuantityResponseDto {
    private Long incentiveId;
    private int quantity;
}
