package com.stablishmentservice.stablishmentservice.dto.incentive.points;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPointsRequest {
    private Long userId;
    private String stablishmentCode;
}
