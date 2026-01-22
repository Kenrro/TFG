package com.productservice.productsservice.enums;

import org.springframework.http.HttpStatus;

public interface IError {
    String getMessage();
    HttpStatus getHttpStatus();
}
