package com.authservice.auth.enums;

import org.springframework.http.HttpStatus;

public enum AuthError implements IError {
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid username or password."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Authentication token has expired."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access is denied."),
    ALREDY_EXIST_A_USER_WITH_THE_SAME_NAME(HttpStatus.CONFLICT, "Alredy exist a user with the same name"),
    ERROR_CREATING_USER(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating the user."),
    ERROR_CREATING_EMPLOYEE(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating the employee."),
    ERROR_LOGIN_CUSTOMER(HttpStatus.INTERNAL_SERVER_ERROR, "Error logging in the customer."),
    ERROR_LOGIN_EMPLOYEE(HttpStatus.INTERNAL_SERVER_ERROR, "Error logging in the employee."),
    INVALID_USER_DATA(HttpStatus.BAD_REQUEST, "Invalid user data provided."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred."), 
    INVALID_ROLE_UPDATE_EMPLOYEE(HttpStatus.BAD_REQUEST, "Invalid role for employee update.")
    ;

    private final String message;
    private final org.springframework.http.HttpStatus httpStatus;

    AuthError(HttpStatus httpStatus, String message) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public org.springframework.http.HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
