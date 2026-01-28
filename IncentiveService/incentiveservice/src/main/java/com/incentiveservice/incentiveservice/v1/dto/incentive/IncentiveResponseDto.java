package com.incentiveservice.incentiveservice.v1.dto.incentive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncentiveResponseDto {

    private Long id;
    private Long productId;
    private String stablishmentCode;
    private int pointsRequired;
    private boolean active = true;
}
