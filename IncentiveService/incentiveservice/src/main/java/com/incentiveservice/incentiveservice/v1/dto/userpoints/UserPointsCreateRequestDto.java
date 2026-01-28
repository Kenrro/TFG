package com.incentiveservice.incentiveservice.v1.dto.userpoints;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPointsCreateRequestDto {
    private Long userId;
    private String stablishmentCode;
}
