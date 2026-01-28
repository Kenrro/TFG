package com.incentiveservice.incentiveservice.v1.dto.incentive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncentiveUpdateDto {
    private int poinstRequired;
}
