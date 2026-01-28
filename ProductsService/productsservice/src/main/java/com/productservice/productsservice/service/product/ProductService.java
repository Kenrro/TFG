package com.productservice.productsservice.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.productservice.productsservice.dto.products.DeleteProductResponsetDto;
import com.productservice.productsservice.dto.products.ProductCreateRequestDto;
import com.productservice.productsservice.dto.products.ProductResponsetDto;
import com.productservice.productsservice.dto.stablishment.StablishmentResponseDto;
import com.productservice.productsservice.entity.Product;
import com.productservice.productsservice.exception.ProductGeneralException;
import com.productservice.productsservice.jwt.JwtUtil;
import com.productservice.productsservice.service.WebClientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final WebClientService webClientService;
    private final ProductCRUDService productCRUDService;
    private final JwtUtil jwtUtil;
    @Value("${app.servicescredential.establishment-service-stablishment-url}") private String stablishmentServiceUrl;
    @Value("${app.servicescredential.incentive-service-incentive-url}") private String incentiveServiceUrl;

    public void createProduct(ProductCreateRequestDto request) {
        
        webClientService.secureGetMethod(stablishmentServiceUrl + "/get-stablishment-by-code/{code}", StablishmentResponseDto.class, request.getStablishmentCode()).getCode();
        
        productCRUDService.create(request);
    }
    public void createProducts(List<DeleteProductResponsetDto> products) {
        productCRUDService.createAll(products);
    }
    public ProductResponsetDto getProductById(Long id) {
        Product product = productCRUDService.findById(id);
        return ProductResponsetDto.builder()
            .id(product.getId())
            .available(product.isAvailable())
            .description(product.getDescription())
            .name(product.getName())
            .price(product.getPrice())
            .stablishmentCode(product.getStablishmentCode())
            .build();
    }
    public List<ProductResponsetDto> getAllProductsByCode(String code) {
        return productCRUDService.findAllByStablishmentCode(code).stream().map(product ->
            ProductResponsetDto.builder()
            .id(product.getId())
            .available(product.isAvailable())
            .description(product.getDescription())
            .name(product.getName())
            .price(product.getPrice())
            .stablishmentCode(product.getStablishmentCode())
            .build()
        ).toList();
    }
    public void updateProduct(Long id, ProductCreateRequestDto request) {
        productCRUDService.updateProduct(id, request);
    }
    public void deleteProductById(Long id, String token) {
        try {

            token = jwtUtil.cleanJwtToken(token);
            String code = jwtUtil.getClaim(token, "establishmentCode", String.class);
            productCRUDService.deleteById(id, code);
            deleteIncentiveFromIncentiveService(id);
        } catch (ProductGeneralException e) {
            throw e;
        }
    }
        private void deleteIncentiveFromIncentiveService(
            Long productId
        ) {
            webClientService.secureDeleteMethod(
                stablishmentServiceUrl + "/delete-by-product-id/{productId}", 
                Void.class, 
                productId);
        }
    public List<DeleteProductResponsetDto> deleteAllProductsByCode(String stablishmentCode) {
        List<DeleteProductResponsetDto> deletedProducts = productCRUDService.findAllByStablishmentCode(stablishmentCode).stream().map(product -> 
            DeleteProductResponsetDto.builder()
            .id(product.getId())
            .available(product.isAvailable())
            .description(product.getDescription())
            .name(product.getName())
            .price(product.getPrice())
            .stablishmentCode(product.getStablishmentCode())
            .build()
        ).toList();
        try{
            webClientService.secureDeleteMethod(
                incentiveServiceUrl + "/delete-by-stablishment/{stablishmentCode}", 
                Void.class, 
                stablishmentCode);
            productCRUDService.deleteProductsByStablishment(stablishmentCode);

        } catch(ProductGeneralException e) {

            throw e;
        }
        return deletedProducts;
    }
    public List<ProductResponsetDto> getAllProductsByIds(List<Long> ids) {
        return productCRUDService.findAllByIds(ids)
        .stream()
        .map(product -> 
            ProductResponsetDto.builder()
            .description(product.getDescription())
            .name(product.getName())
            .id(product.getId())
            .price(product.getPrice())
            .stablishmentCode(product.getStablishmentCode())
            .build()
        
        ).toList();

    }
}
