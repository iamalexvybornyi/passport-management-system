package com.iamalexvybornyi.passportmanagementsystem.controller.error;

import com.iamalexvybornyi.passportmanagementsystem.exception.BusinessValidationException;
import com.iamalexvybornyi.passportmanagementsystem.exception.RecordNotFoundException;
import com.iamalexvybornyi.passportmanagementsystem.model.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.*;

@ControllerAdvice
public class ErrorHandlingController {

    @NonNull
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ApiError handleValidationException(@NonNull MethodArgumentNotValidException exception,
                                               @NonNull HttpServletRequest request) {
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), "Validation Error",
                request.getServletPath());
        final BindingResult result = exception.getBindingResult();
        final Map<String, String> validationErrors = new HashMap<>();
        result.getFieldErrors().forEach(fieldError ->
                validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage()));
        apiError.setValidationErrors(validationErrors);
        return apiError;
    }

    @NonNull
    @ExceptionHandler({BusinessValidationException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    private ApiError handleBusinessValidationException(@NonNull BusinessValidationException exception,
                                                       @NonNull HttpServletRequest request) {
        final ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Business Validation Error",
                request.getServletPath());
        apiError.setValidationErrors(exception.getValidationErrors());
        return apiError;
    }

    @NonNull
    @ExceptionHandler({RecordNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ApiError handleNotFoundException(@NonNull RecordNotFoundException exception,
                                             @NonNull HttpServletRequest request) {
        return new ApiError(HttpStatus.NOT_FOUND.value(), exception.getReason(),
                request.getServletPath());
    }

    @NonNull
    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ApiError handleRuntimeException(@NonNull RuntimeException exception, @NonNull HttpServletRequest request) {
        return new ApiError(HttpStatus.BAD_REQUEST.value(), "Invalid input data",
                request.getServletPath());
    }
}
