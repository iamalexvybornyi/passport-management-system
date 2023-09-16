package com.iamalexvybornyi.passportmanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    @NonNull
    private long timestamp = new Date().getTime();

    @NonNull
    private int status;

    @NonNull
    private String message;

    @NonNull
    private String url;

    @NonNull
    private Map<String, String> validationErrors;

    public ApiError(@NonNull int status, @NonNull String message, @NonNull String url) {
        this.status = status;
        this.message = message;
        this.url = url;
    }
}
