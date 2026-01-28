package com.incentiveservice.incentiveservice.v1.dto.incentive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncentiveCreateRequestDto {
    private Long productId;
    private String stablishmentCode;
    private int pointsRequired;
}
