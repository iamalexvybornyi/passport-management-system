package com.iamalexvybornyi.passportmanagementsystem.controller.error;

import com.iamalexvybornyi.passportmanagementsystem.exception.BusinessValidationException;
import com.iamalexvybornyi.passportmanagementsystem.exception.RecordNotFoundException;
import com.iamalexvybornyi.passportmanagementsystem.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@ControllerAdvice
public class ErrorHandlingController {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ApiError handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), "Validation Error",
                request.getServletPath());
        BindingResult result = exception.getBindingResult();
        Map<String, String> validationErrors = new HashMap<>();
        result.getFieldErrors().forEach(fieldError ->
                validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage()));
        apiError.setValidationErrors(validationErrors);
        return apiError;
    }

    @ExceptionHandler({BusinessValidationException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    private ApiError handleBusinessValidationException(BusinessValidationException exception,
                                                       HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Business Validation Error",
                request.getServletPath());
        apiError.setValidationErrors(exception.getValidationErrors());
        return apiError;
    }

    @ExceptionHandler({RecordNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ApiError handleNotFoundException(RecordNotFoundException exception, HttpServletRequest request) {
        return new ApiError(HttpStatus.NOT_FOUND.value(), exception.getReason(),
                request.getServletPath());
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ApiError handleRuntimeException(RuntimeException exception, HttpServletRequest request) {
        return new ApiError(HttpStatus.BAD_REQUEST.value(), "Invalid input data",
                request.getServletPath());
    }
}
