package com.iamalexvybornyi.passportmanagementsystem.exception;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Getter
public class BusinessValidationException extends ResponseStatusException {

    @NonNull
    private final Map<String, String> validationErrors;

    public BusinessValidationException(@NonNull String reason, @NonNull Map<String, String> validationErrors) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, reason);
        this.validationErrors = validationErrors;
    }
}
