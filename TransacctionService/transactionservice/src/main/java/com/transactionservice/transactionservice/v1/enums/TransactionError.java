package com.transactionservice.transactionservice.v1.enums;

import org.springframework.http.HttpStatus;



public enum TransactionError implements IError {

    // ===== CREATION =====
    TRANSACTION_CREATION_FAILED(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Failed to create transaction"
    ),
    INVALID_TRANSACTION_TYPE(
        HttpStatus.BAD_REQUEST,
        "Invalid transaction type"
    ),
    INVALID_TRANSACTION_DATA(
        HttpStatus.BAD_REQUEST,
        "Invalid transaction data"
    ),

    // ===== RETRIEVAL =====
    TRANSACTION_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "Transaction not found"
    ),

    // ===== STATUS / FLOW =====
    TRANSACTION_ALREADY_PROCESSED(
        HttpStatus.CONFLICT,
        "Transaction already processed"
    ),
    TRANSACTION_NOT_PENDING(
        HttpStatus.BAD_REQUEST,
        "Transaction is not pending"
    ),
    TRANSACTION_EXPIRED(
        HttpStatus.GONE,
        "Transaction expired"
    ),
    TRANSACTION_CANCELLED(
        HttpStatus.CONFLICT,
        "Transaction has been cancelled"
    ),

    // ===== ACTORS =====
    MISSING_CUSTOMER(
        HttpStatus.BAD_REQUEST,
        "Customer not assigned to transaction"
    ),
    MISSING_SELLER(
        HttpStatus.BAD_REQUEST,
        "Seller not assigned to transaction"
    ),
    INVALID_ACTOR(
        HttpStatus.FORBIDDEN,
        "User is not allowed to process this transaction"
    ),

    // ===== POINTS / INCENTIVES =====
    INSUFFICIENT_POINTS(
        HttpStatus.BAD_REQUEST,
        "Insufficient points to complete transaction"
    ),
    INCENTIVE_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "Incentive not found"
    ),
    INCENTIVE_INACTIVE(
        HttpStatus.BAD_REQUEST,
        "Incentive is not active"
    ),

    // ===== QR =====
    INVALID_QR_CODE(
        HttpStatus.BAD_REQUEST,
        "Invalid QR code"
    ),
    QR_ALREADY_USED(
        HttpStatus.CONFLICT,
        "QR code already used"
    ),

    // ===== COMMUNICATION =====
    SERVICE_COMMUNICATION_FAILED(
        HttpStatus.BAD_GATEWAY,
        "Failed to communicate with external service"
    ),

    // ===== ROLLBACK =====
    TRANSACTION_ROLLBACK_FAILED(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Failed to rollback transaction"
    ),

    // ===== UNEXPECTED =====
    UNEXPECTED_ERROR(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Unexpected transaction error"
    );
    TransactionError(HttpStatus httpStatus, String message) {
        this.message = message;
        this.status = httpStatus;
    }

    private final HttpStatus status;
    private final String message;
    @Override
    public String getMessage() {
        return message;    
    }
    @Override
    public HttpStatus getHttpStatus() {
        return status;    
    }
}
