package com.stablishmentservice.stablishmentservice.dto.stablishment;

import com.stablishmentservice.stablishmentservice.dto.incentive.configuration.StablishmentConfigurationResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StablishmentWithConfigurationResponseDto {
    private StablishmentResponseDto stablishment;
    private StablishmentConfigurationResponseDto configuration;
}
