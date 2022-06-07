package com.iamalexvybornyi.passportmanagementsystem.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PropertyReaderUtil {

    @Value("${passport.management.system.test.constraints.passport.type.message}")
    private String passportTypeValidationMessage;

    @Value("${passport.management.system.test.constraints.passport.status.message}")
    private String passportStatusValidationMessage;

    @Value("${passport.management.system.test.constraints.given.date.message}")
    private String givenDateValidationMessage;

    @Value("${passport.management.system.test.constraints.department.code.message}")
    private String departmentCodeValidationMessage;

    @Value("${passport.management.system.test.constraints.birth.date.format.message}")
    private String birthDateFormatValidationMessage;

    @Value("${passport.management.system.test.constraints.birth.date.validation.message}")
    private String birthDateValidationMessage;

    @Value("${passport.management.system.test.constraints.unique.passport.number.message}")
    private String uniquePassportValidationMessage;
}
