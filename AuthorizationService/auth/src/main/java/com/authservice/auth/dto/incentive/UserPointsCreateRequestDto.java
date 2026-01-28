package com.authservice.auth.dto.incentive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPointsCreateRequestDto {
    private Long userId;
    private String stablishmentCode;
}
