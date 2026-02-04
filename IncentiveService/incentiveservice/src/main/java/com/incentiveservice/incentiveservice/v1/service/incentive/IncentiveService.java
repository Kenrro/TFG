package com.incentiveservice.incentiveservice.v1.service.incentive;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.incentiveservice.incentiveservice.v1.dto.incentive.IncentiveCreateRequestDto;
import com.incentiveservice.incentiveservice.v1.dto.incentive.IncentiveResponseDto;
import com.incentiveservice.incentiveservice.v1.dto.incentive.IncentiveUpdateDto;
import com.incentiveservice.incentiveservice.v1.dto.incentive.ProductIncentiveResponseDto;
import com.incentiveservice.incentiveservice.v1.dto.incentive.ProductsIncentivesDto;
import com.incentiveservice.incentiveservice.v1.dto.product.ProductResponseDto;
import com.incentiveservice.incentiveservice.v1.dto.product.ProductsResponseDto;
import com.incentiveservice.incentiveservice.v1.entity.Incentive;
import com.incentiveservice.incentiveservice.v1.jwt.JwtUtil;
import com.incentiveservice.incentiveservice.v1.service.WebClientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncentiveService {


    @Value("${app.servicescredential.product-service-url}") String productServiceUrl;
    private final IncentiveCRUDService incentiveCRUDService;
    private final WebClientService WebClientService;
    private final JwtUtil jwtUtil;

    // =========================================================
    // GET INCENTIVE
    // =========================================================
    public ProductsIncentivesDto getIncentive(
        String stablishmentCode
    ) {
        List<IncentiveResponseDto> incentives = incentiveCRUDService.getIncentivesByStablishmentCode(stablishmentCode)
        .stream()
        .map(incentive->
            IncentiveResponseDto.builder()
            .id(incentive.getId())
            .pointsRequired(incentive.getPointsRequired())
            .stablishmentCode(incentive.getStablishmentCode())
            .productId(incentive.getProductId())
            .active(incentive.isActive())
            .build()
        ).toList();
        ProductsResponseDto products = getProductsFromProductService(
            incentives.stream().map(incentive-> incentive.getProductId()).toList()
        );
        products.getProducts().stream().forEach(product -> System.out.println(product.getId()));
        List<ProductIncentiveResponseDto> productsIncentive = products.getProducts().stream().map(product ->
            ProductIncentiveResponseDto.builder()
            .productResponsetDto(product)
            .incentiveResponseDto(incentives.stream().filter(incentive -> incentive.getProductId().equals(product.getId())).findFirst().orElse(null))
            .build()
        ).toList(); // TODO: repair the get to products from product service mmh
        return ProductsIncentivesDto.builder()
        .incentives(productsIncentive)
        .build();
    }
    // =========================================================
    // GET INCENTIVE BY ID
    // =========================================================
    public ProductIncentiveResponseDto getIncentiveById(
        Long id
    ) {
        Incentive incentive = incentiveCRUDService.getIncentivesById(id);
        ProductResponseDto product = getProductsFromProductService(List.of(incentive.getProductId())).getProducts().get(0);
        return ProductIncentiveResponseDto.builder()
        .incentiveResponseDto(
            IncentiveResponseDto.builder()
            .id(incentive.getId())
            .pointsRequired(incentive.getPointsRequired())
            .stablishmentCode(incentive.getStablishmentCode())
            .productId(incentive.getProductId())
            .active(incentive.isActive())
            .build()
        )
        .productResponsetDto(product)
        .build();
    }
    // Find products in product service by ids
        private ProductsResponseDto getProductsFromProductService(List<Long> ids) {
            return WebClientService.securePostMethod(
                productServiceUrl + "/get-all-by-ids", 
                ids, 
                ProductsResponseDto.class);
        }
    // =========================================================
    // CREATE INCENTIVE
    // =========================================================
    public void createIncentive(
        IncentiveCreateRequestDto request
    ) {
        checkProductExists(request.getProductId());
        incentiveCRUDService.create(request);
    }
        private void checkProductExists(
            Long id
        ) {
            WebClientService.secureGetMethod(
                productServiceUrl + "/get-by-id/{id}", 
                ProductsResponseDto.class, 
                id);
        }
    // =========================================================
    // UPDATE INCENTIVE
    // =========================================================
    public void updateIncentive(
        IncentiveUpdateDto request,
        Long id,
        String token
    ) {
        String codeByToken = jwtUtil.cleanJwtToken(token);
        codeByToken = jwtUtil.getClaim(codeByToken, "establishmentCode", String.class);
        incentiveCRUDService.update(request, id, codeByToken);
    }
    // =========================================================
    // DELETE BY ID
    // =========================================================
    public void deleteById(
        Long id,
        String token
    ) {
        String codeByToken = jwtUtil.cleanJwtToken(token);
        codeByToken = jwtUtil.getClaim(codeByToken, "establishmentCode", String.class);
        incentiveCRUDService.delete(id, codeByToken);
    }
    // =========================================================
    // DELETE ALL BY STABLISHMENT CODE WHEN ELIMINATED STABLISHMENT
    // =========================================================
    public void deleteAllByStablishmentCode(
        String stablishmentCode
    ) {
        incentiveCRUDService.deleteAllByStablishmentCode(stablishmentCode);
    }
    // =========================================================
    // DELETE BY PRODUCT ID WHEN ELIMINATED PRODUCT
    // =========================================================
    public void deleteByProductId(
        Long productId
    ) {
        incentiveCRUDService.deleteByProductId(productId);
    }
}
