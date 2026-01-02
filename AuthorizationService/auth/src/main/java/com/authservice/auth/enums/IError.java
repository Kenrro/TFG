package com.authservice.auth.enums;

import org.springframework.http.HttpStatus;

public interface IError {
    String getMessage();
    HttpStatus getHttpStatus();
}
