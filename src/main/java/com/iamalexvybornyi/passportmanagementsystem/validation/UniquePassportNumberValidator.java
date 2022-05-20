package com.iamalexvybornyi.passportmanagementsystem.validation;

import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import com.iamalexvybornyi.passportmanagementsystem.repository.PassportRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniquePassportNumberValidator implements ConstraintValidator<UniquePassportNumber, String> {

    private final PassportRepository passportRepository;

    @Autowired
    public UniquePassportNumberValidator(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;
    }

    @Override
    public boolean isValid(String passportNumber, ConstraintValidatorContext constraintValidatorContext) {
        Passport passport = passportRepository.findByPassportNumber(passportNumber);
        return passport == null;
    }
}
