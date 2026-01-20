package com.stablishmentservice.stablishmentservice.dto.stablishment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStablishmentResponseDto {
    private Long userId;
    private Long stablishmentId;
}
