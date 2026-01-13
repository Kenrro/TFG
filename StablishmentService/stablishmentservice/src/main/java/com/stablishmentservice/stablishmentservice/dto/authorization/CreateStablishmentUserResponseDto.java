package com.stablishmentservice.stablishmentservice.dto.authorization;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateStablishmentUserResponseDto {
    private Long adminId;
}
