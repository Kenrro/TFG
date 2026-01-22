package com.productservice.productsservice.dto.products;

import lombok.Data;

@Data
public class ProductCreateRequestDto {
    private String stablishmentCode;
    private String name;
    private String description;
    private float price;
    private boolean available = true;
}
