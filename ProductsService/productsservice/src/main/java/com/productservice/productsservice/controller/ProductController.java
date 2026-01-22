package com.productservice.productsservice.controller;

import org.springframework.web.bind.annotation.RestController;

import com.productservice.productsservice.dto.products.DeletedProductsResponseDto;
import com.productservice.productsservice.dto.products.ProductCreateRequestDto;
import com.productservice.productsservice.dto.products.ProductResponsetDto;
import com.productservice.productsservice.service.product.ProductService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "Endpoints for managing products")
public class ProductController {

    private final ProductService productService;

    
    /**
     * Get product by ID.
     * Only accessible by ADMIN or SELLER.
     */
    @Operation(summary = "Get product by ID", description = "Retrieve a single product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ProductResponsetDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Get all products by establishment code.
     * Only accessible by ADMIN or SELLER.
     */
    @Operation(summary = "Get all products by establishment code", description = "Retrieve all products for a specific establishment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Establishment not found")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    @GetMapping("/get-all-by-stablishment-code/{code}")
    public ResponseEntity<List<ProductResponsetDto>> getAllByStablishmentCode(@PathVariable String code) {
        return ResponseEntity.ok(productService.getAllProductsByCode(code));
    }

    /**
     * Create a new product.
     * Only accessible by ADMIN.
     */
    @Operation(summary = "Create a new product", description = "Create a new product for the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody ProductCreateRequestDto request) {
        productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Update an existing product.
     * Only accessible by ADMIN.
     */
    @Operation(summary = "Update a product", description = "Update details of an existing product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody ProductCreateRequestDto requestDto) {
        productService.updateProduct(id, requestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a product by ID.
     * Only accessible by ADMIN.
     */
    @Operation(summary = "Delete a product by ID", description = "Delete a single product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<Void> deleteById(
        @PathVariable Long id,
        @RequestHeader("Authorization") String token
    ) {
        productService.deleteProductById(id, token);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete all products by establishment code.
     * Only accessible by SERVICE role (used in saga pattern).
     */
    @Operation(summary = "Delete all products by establishment code", description = "Used by SERVICE to remove all products for an establishment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Products not found")
    })
    @PreAuthorize("hasRole('SERVICE')")
    @DeleteMapping("/delete-by-code/{code}")
    public ResponseEntity<DeletedProductsResponseDto> deleteByCode(@PathVariable String code) {
        DeletedProductsResponseDto deletedProductsResponseDto = 
            new DeletedProductsResponseDto(productService.deleteAllProductsByCode(code));
        return ResponseEntity.ok(deletedProductsResponseDto);
    }

    /**
     * Rollback deleted products.
     * Only accessible by SERVICE role (used in saga pattern).
     */
    @Operation(summary = "Rollback deleted products", description = "Used by SERVICE to restore previously deleted products in case of failure")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products restored successfully"),
        @ApiResponse(responseCode = "500", description = "Failed to restore products")
    })
    @PreAuthorize("hasRole('SERVICE')")
    @PostMapping("/rollback-deleted-products")
    public ResponseEntity<Void> rollbackDeletedProducts(@RequestBody DeletedProductsResponseDto responseDto) {
        productService.createProducts(responseDto.getProducts());        
        return ResponseEntity.ok().build();
    }
}
