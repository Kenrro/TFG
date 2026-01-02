package com.authservice.auth.exception;

import org.springframework.http.HttpStatus;

import com.authservice.auth.enums.IError;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AuthException extends RuntimeException {
    private final String message;
    private final HttpStatus httpStatus;
    
    public <T extends IError> AuthException(T error) {
        super(error.getMessage());
        this.httpStatus = error.getHttpStatus();
        this.message = error.getMessage();
    }   

}
