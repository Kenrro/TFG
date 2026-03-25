package com.productservice.productsservice.controller;

import org.springframework.web.bind.annotation.RestController;

import com.productservice.productsservice.dto.products.DeletedProductsResponseDto;
import com.productservice.productsservice.dto.products.ProductCreateRequestDto;
import com.productservice.productsservice.dto.products.ProductResponsetDto;
import com.productservice.productsservice.dto.products.ProductsResponseDto;
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

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;



@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "Endpoints for managing products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/ping")
    public String ping() {
        return "PRODUCT SERVICE OK";
    }
    /**
     * Get product by ID.
     * Only accessible by ADMIN or SELLER.
     */
    @Operation(
        summary = "Get product by ID",
        description = "Retrieve a single product by its ID. Requires ADMIN or SELLER role.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Product retrieved successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponsetDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Product example",
                            summary = "Example product response",
                            value = """
                            {
                            "id": 10,
                            "name": "Hamburguesa Clásica",
                            "description": "Hamburguesa con carne 100% vacuno",
                            "price": 9.99,
                            "available": true,
                            "category": "FOOD"
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Product not found"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - ADMIN or SELLER role required"
            )
        }
    )
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SELLER')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponsetDto> getById(
            @PathVariable
            @Parameter(description = "ID of the product", example = "10")
            Long id
    ) {

        return ResponseEntity.ok(
                productService.getProductById(id)
        );
    }


    /**
     * Get all products by establishment code.
     * Only accessible by ADMIN or SELLER.
     */
    @Operation(
        summary = "Get all products by establishment code",
        description = "Retrieve all products for a specific establishment. Requires ADMIN or SELLER role.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Products retrieved successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponsetDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Products list",
                            summary = "Example products response",
                            value = """
                            [
                            {
                                "id": 10,
                                "name": "Hamburguesa Clásica",
                                "description": "Hamburguesa con carne 100% vacuno",
                                "price": 9.99,
                                "available": true,
                                "category": "FOOD"
                            },
                            {
                                "id": 11,
                                "name": "Pizza Margarita",
                                "description": "Pizza clásica italiana",
                                "price": 12.50,
                                "available": true,
                                "category": "FOOD"
                            }
                            ]
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Establishment not found"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - ADMIN or SELLER role required"
            )
        }
    )
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SELLER')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/stablishments/{code}/products")
    public ResponseEntity<List<ProductResponsetDto>> getAllByStablishmentCode(
            @PathVariable
            @Parameter(description = "Establishment code", example = "EST-9F3A2B")
            String code
    ) {

        return ResponseEntity.ok(
                productService.getAllProductsByCode(code)
        );
    }

    @PreAuthorize(
    "hasAuthority('ADMIN') or hasAuthority('SELLER') or " +
    "hasAuthority('CUSTOMER') or hasRole('SERVICE')"
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/products/search")
    @Operation(
        summary = "Get products by IDs",
        description = "Retrieve multiple products by their IDs. Accessible by ADMIN, SELLER, CUSTOMER or SERVICE roles.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Products retrieved successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductsResponseDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Products response",
                            summary = "Example products response",
                            value = """
                            {
                            "products": [
                                {
                                "id": 10,
                                "name": "Hamburguesa Clásica",
                                "description": "Hamburguesa con carne 100% vacuno",
                                "price": 9.99,
                                "available": true,
                                "category": "FOOD"
                                },
                                {
                                "id": 11,
                                "name": "Pizza Margarita",
                                "description": "Pizza clásica italiana",
                                "price": 12.50,
                                "available": true,
                                "category": "FOOD"
                                }
                            ]
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid product IDs"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - insufficient permissions"
            )
        }
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "List of product IDs",
        required = true,
        content = @Content(
            examples = {
                @ExampleObject(
                    name = "Product IDs",
                    summary = "Example list of product IDs",
                    value = """
                    [10, 11, 15]
                    """
                )
            }
        )
    )
    public ResponseEntity<ProductsResponseDto> getProductsByIds(
            @RequestBody List<Long> ids
    ) {

        ProductsResponseDto products = ProductsResponseDto.builder()
                .products(productService.getAllProductsByIds(ids))
                .build();

        return ResponseEntity.ok(products);
    }

    /**
     * Create a new product.
     * Only accessible by ADMIN.
     */
    @Operation(
        summary = "Create product",
        description = "Creates a new product. Requires ADMIN role.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Product created successfully"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid request data"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - ADMIN role required"
            )
        }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Product data to create",
        required = true,
        content = @Content(
            schema = @Schema(implementation = ProductCreateRequestDto.class),
            examples = @ExampleObject(
                name = "Create product",
                summary = "Example request",
                value = """
                {
                "name": "Hamburguesa Clásica",
                "description": "Hamburguesa con carne 100% vacuno",
                "price": 9.99,
                "available": true
                }
                """
            )
        )
    )
    public ResponseEntity<Void> create(
            @RequestBody ProductCreateRequestDto request,
            @RequestHeader("Authorization") String token
    ) {

        productService.createProduct(token, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * Update an existing product.
     * Only accessible by ADMIN.
     */
    @Operation(
        summary = "Update a product",
        description = "Update details of an existing product. Requires ADMIN role.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Product updated successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Product not found"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid request data"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - ADMIN role required"
            )
        }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Updated product data",
        required = true,
        content = @Content(
            schema = @Schema(implementation = ProductCreateRequestDto.class),
            examples = {
                @ExampleObject(
                    name = "Update product example",
                    summary = "Valid product update request",
                    value = """
                    {
                    "name": "Hamburguesa Clásica",
                    "description": "Hamburguesa con carne 100% vacuno",
                    "price": 9.99,
                    "available": true
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Void> update(
            @PathVariable
            @Parameter(description = "ID of the product to update", example = "10")
            Long id,

            @RequestBody ProductCreateRequestDto requestDto
    ) {

        productService.updateProduct(id, requestDto);
        return ResponseEntity.ok().build();
    }


    /**
     * Delete a product by ID.
     * Only accessible by ADMIN.
     */
    @Operation(
        summary = "Delete a product by ID",
        description = "Delete a single product by its ID. Requires ADMIN role.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Product deleted successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Product not found"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - ADMIN role required"
            )
        }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable
            @Parameter(description = "ID of the product to delete", example = "10")
            Long id,

            @RequestHeader("Authorization")
            @Parameter(description = "Bearer token for authentication")
            String token
    ) {

        productService.deleteProductById(id, token);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete all products by establishment code.
     * Only accessible by SERVICE role (used in saga pattern).
     */
    @Operation(
        summary = "Delete all products by establishment code",
        description = "Used by SERVICE to remove all products for a specific establishment.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Products deleted successfully",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DeletedProductsResponseDto.class),
                    examples = {
                        @ExampleObject(
                            name = "Deleted products response",
                            summary = "Example response",
                            value = """
                            {
                            "deletedCount": 12
                            }
                            """
                        )
                    }
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Products not found"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - SERVICE role required"
            )
        }
    )
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/stablishments/{code}/products")
    public ResponseEntity<DeletedProductsResponseDto> deleteByCode(
            @PathVariable
            @Parameter(description = "Establishment code", example = "EST-9F3A2B")
            String code
    ) {

        DeletedProductsResponseDto deletedProductsResponseDto =
                new DeletedProductsResponseDto(
                        productService.deleteAllProductsByCode(code)
                );

        return ResponseEntity.ok(deletedProductsResponseDto);
    }


    /**
     * Rollback deleted products.
     * Only accessible by SERVICE role (used in saga pattern).
     */
    @Operation(
        summary = "Rollback deleted products",
        description = "Used by SERVICE to restore previously deleted products in case of failure.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Products restored successfully"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Failed to restore products"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden - SERVICE role required"
            )
        }
    )
    @Hidden
    @PreAuthorize("hasRole('SERVICE')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/rollback-deleted-products")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Deleted products data used to restore products",
        required = true,
        content = @Content(
            schema = @Schema(implementation = DeletedProductsResponseDto.class),
            examples = {
                @ExampleObject(
                    name = "Rollback products example",
                    summary = "Example rollback request",
                    value = """
                    {
                    "products": [
                        {
                        "id": 10,
                        "name": "Hamburguesa Clásica",
                        "description": "Hamburguesa con carne 100% vacuno",
                        "price": 9.99,
                        "available": true,
                        "category": "FOOD",
                        "stablishmentCode": "EST-9F3A2B"
                        },
                        {
                        "id": 11,
                        "name": "Pizza Margarita",
                        "description": "Pizza clásica italiana",
                        "price": 12.50,
                        "available": true,
                        "category": "FOOD",
                        "stablishmentCode": "EST-9F3A2B"
                        }
                    ]
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Void> rollbackDeletedProducts(
            @RequestBody DeletedProductsResponseDto responseDto
    ) {

        productService.createProducts(responseDto.getProducts());
        return ResponseEntity.ok().build();
    }
    @Hidden
    @PreAuthorize("hasAuthority('SERVICE')")
    @GetMapping("/stablishment/{stablishmentCode}/dashboard")
    public ResponseEntity<Integer> getProdutcInformation(
        @PathVariable String stablishmentCode
    ) {
        return ResponseEntity.ok(productService.getProductsQuantity(stablishmentCode)); // Replace 42 with actual product information
    }
    
}
