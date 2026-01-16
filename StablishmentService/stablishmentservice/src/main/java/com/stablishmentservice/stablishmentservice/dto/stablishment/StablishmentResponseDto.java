package com.stablishmentservice.stablishmentservice.dto.stablishment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StablishmentResponseDto {
    private String name;
    private String description;
    private String address;
    private String code;
}
