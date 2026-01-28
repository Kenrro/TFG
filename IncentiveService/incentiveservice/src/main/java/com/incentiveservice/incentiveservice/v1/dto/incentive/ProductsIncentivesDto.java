package com.incentiveservice.incentiveservice.v1.dto.incentive;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class ProductsIncentivesDto {
    private List<ProductIncentiveResponseDto> incentives;
}
