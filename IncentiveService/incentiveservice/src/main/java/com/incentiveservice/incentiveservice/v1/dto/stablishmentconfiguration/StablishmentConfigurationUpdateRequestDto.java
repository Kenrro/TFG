package com.incentiveservice.incentiveservice.v1.dto.stablishmentconfiguration;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StablishmentConfigurationUpdateRequestDto {
     @Min(value = 1, message = "The value must be greater than or equal to 0")
    private int point_per_euro;
}
