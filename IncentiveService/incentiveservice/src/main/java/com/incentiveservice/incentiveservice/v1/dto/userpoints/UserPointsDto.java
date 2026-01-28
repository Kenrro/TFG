package com.incentiveservice.incentiveservice.v1.dto.userpoints;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPointsDto {

    private Long userId;
    private String stablishmentCode;
    private int balance;
}
