package com.productservice.productsservice.dto.products;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeletedProductsResponseDto {
    private List<DeleteProductResponsetDto> products;
}
