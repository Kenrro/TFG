package com.incentiveservice.incentiveservice.v1.dto.incentive;

import com.incentiveservice.incentiveservice.v1.dto.product.ProductResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductIncentiveResponseDto {
    private IncentiveResponseDto incentiveResponseDto;
    private ProductResponseDto productResponsetDto;
}
