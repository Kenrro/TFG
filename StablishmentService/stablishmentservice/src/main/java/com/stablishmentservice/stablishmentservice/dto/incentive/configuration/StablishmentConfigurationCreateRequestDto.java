package com.stablishmentservice.stablishmentservice.dto.incentive.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StablishmentConfigurationCreateRequestDto {

    private int pointsPerEuro = 10;
    private String stablishmentCode;
}
