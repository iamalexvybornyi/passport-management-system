package com.iamalexvybornyi.passportmanagementsystem.validation;

import com.iamalexvybornyi.passportmanagementsystem.dto.passport.CreatePassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.person.CreatePersonDto;
import com.iamalexvybornyi.passportmanagementsystem.exception.BusinessValidationException;
import com.iamalexvybornyi.passportmanagementsystem.repository.PassportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class CommonFieldValidationUtil {

    private final PassportRepository passportRepository;

    @Autowired
    public CommonFieldValidationUtil(
            PassportRepository passportRepository
    ) {
        this.passportRepository = passportRepository;
    }

    public void verifyCreatePersonDtoForBusinessRequirements(CreatePersonDto createPersonDto) {
        Map<String, String> errors = new HashMap<>();
        if (createPersonDto.getBirthDate().compareTo(LocalDate.now().minusYears(14)) >= 0) {
            errors.put("birthDate", "Person can't be younger than 14 years old!");
        }

        if (errors.size() > 0) {
            throw new BusinessValidationException("Business Validation Error", errors);
        }
    }

    public void verifyCreatePassportDtoForBusinessRequirements(CreatePassportDto createPassportDto) {
        Map<String, String> errors = new HashMap<>();
        if (passportRepository.findByPassportNumber(createPassportDto.getPassportNumber()) != null) {
            errors.put("passportNumber", "Passport number must be unique!");
        }

        if (errors.size() > 0) {
            throw new BusinessValidationException("Business Validation Error", errors);
        }
    }
}
