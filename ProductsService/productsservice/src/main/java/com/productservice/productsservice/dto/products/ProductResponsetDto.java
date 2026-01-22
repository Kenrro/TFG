package com.productservice.productsservice.dto.products;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponsetDto {
    private Long id;
    private String stablishmentCode;
    private String name;
    private String description;
    private float price;
    private boolean available;
}
