package com.productservice.productsservice.dto.stablishment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StablishmentResponseDto {
    private String name;
    private String description;
    private String address;
    private String code;
}
