package com.productservice.productsservice.dto.products;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ProductsResponseDto {
    private List<ProductResponsetDto> products;
}
