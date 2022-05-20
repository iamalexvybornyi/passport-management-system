package com.iamalexvybornyi.passportmanagementsystem.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniquePassportNumberValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniquePassportNumber {

    String message() default "Passport number must be unique!";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
