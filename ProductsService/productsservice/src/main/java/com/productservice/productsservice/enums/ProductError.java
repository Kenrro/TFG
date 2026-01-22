package com.productservice.productsservice.enums;

import org.springframework.http.HttpStatus;

public enum ProductError implements IError {
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Product not found"),
    PRODUCT_ALREADY_EXISTS(HttpStatus.CONFLICT, "Product already exists"),
    PRODUCT_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create product"),
    PRODUCT_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update product"),
    PRODUCT_DELETION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete product"),
    INVALID_PRODUCT_DATA(HttpStatus.BAD_REQUEST, "Invalid product data provided"),
    UNAUTHORIZED_PRODUCT_ACCESS(HttpStatus.FORBIDDEN, "You are not authorized to modify this product"),
    PRODUCT_STABLISHMENT_MISMATCH(HttpStatus.BAD_REQUEST, "Product does not belong to the given stablishment"),
    SERVICE_COMMUNICATION_FAILED(HttpStatus.BAD_GATEWAY, "Failed to communicate with external service"),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred")
    ;

    private final String message;
    private final HttpStatus httpStatus;

    ProductError(HttpStatus httpStatus, String message) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
