package com.stablishmentservice.stablishmentservice.dto.incentive.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StablishmentConfigurationResponseDto {

    private String stablishmentCode;
    private int points_per_euro;
}
