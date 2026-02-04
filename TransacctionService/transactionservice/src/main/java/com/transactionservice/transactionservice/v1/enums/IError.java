package com.transactionservice.transactionservice.v1.enums;

import org.springframework.http.HttpStatus;

public interface IError {
    String getMessage();
    HttpStatus getHttpStatus();
}

