package com.incentiveservice.incentiveservice.v1.dto.stablishmentconfiguration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StablishmentConfigurationCreateRequestDto {
   
    private int pointsPerEuro = 10;
    private String stablishmentCode;
}
