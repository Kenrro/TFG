package com.incentiveservice.incentiveservice.v1.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionPointsResponseDto {
    private int pointsInvolved;
}
