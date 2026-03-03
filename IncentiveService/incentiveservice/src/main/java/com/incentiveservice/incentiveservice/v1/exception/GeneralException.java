package com.incentiveservice.incentiveservice.v1.exception;

import org.springframework.http.HttpStatus;

import com.incentiveservice.incentiveservice.v1.dto.errors.ErrorDto;
import com.incentiveservice.incentiveservice.v1.enums.IError;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class GeneralException extends RuntimeException{

    private final String from;
    private final HttpStatus httpStatus;

    public <T extends IError> GeneralException(T error) {
        super(error.getMessage());
        this.from = "Incentive Service";
        this.httpStatus = error.getHttpStatus();
    }
    public <T extends IError> GeneralException(T error, Throwable cause) {
        super(error.getMessage(), cause);
        this.from = "Incentive Service";
        this.httpStatus = error.getHttpStatus();
        log.error("❌ Request interrupted by the following error {}", cause);
    }

    public GeneralException(ErrorDto errorDto) {
        super(errorDto.getMessage());
        this.from = errorDto.getFrom();
        this.httpStatus = HttpStatus.resolve(parseStatus(errorDto.getStatus()));
    }

    private int parseStatus(String status) {
        try {
            return Integer.parseInt(status);
        } catch (NumberFormatException e) {
            try {
                return HttpStatus.valueOf(status).value();
            } catch (IllegalArgumentException ex) {
                return HttpStatus.INTERNAL_SERVER_ERROR.value();
            }
        }
    }
}
