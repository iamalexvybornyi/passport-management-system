package com.iamalexvybornyi.passportmanagementsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Getter
public class BusinessValidationException extends ResponseStatusException {

    private final Map<String, String> validationErrors;

    public BusinessValidationException(String reason, Map<String, String> validationErrors) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, reason);
        this.validationErrors = validationErrors;
    }
}
