package com.iamalexvybornyi.passportmanagementsystem.controller.error;

import com.iamalexvybornyi.passportmanagementsystem.model.ApiError;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RestController
@AllArgsConstructor
public class ErrorHandler implements ErrorController {

    @NonNull
    private final ErrorAttributes errorAttributes;

    @NonNull
    @RequestMapping("/error")
    ApiError handleError(@NonNull WebRequest webRequest) {
        final Map<String, Object> attributes = errorAttributes.getErrorAttributes(webRequest,
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
        final String message = (String) attributes.get("message");
        final String url = (String) attributes.get("path");
        final int status = (int) attributes.get("status");
        return new ApiError(status, message, url);
    }
}
