package com.iamalexvybornyi.passportmanagementsystem.controller.error;

import com.iamalexvybornyi.passportmanagementsystem.model.ApiError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RestController
public class ErrorHandler implements ErrorController {

    private final ErrorAttributes errorAttributes;

    @Autowired
    public ErrorHandler(
            ErrorAttributes errorAttributes
    ) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    ApiError handleError(WebRequest webRequest) {
        Map<String, Object> attributes = errorAttributes.getErrorAttributes(webRequest,
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
        String message = (String) attributes.get("message");
        String url = (String) attributes.get("path");
        int status = (int) attributes.get("status");
        return new ApiError(status, message, url);
    }
}
