package com.stablishmentservice.stablishmentservice.dto.stablishment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StablishmentAndAdminResponseDto {
    private String name;
    private String description;
    private String address;
    private String code;
}
