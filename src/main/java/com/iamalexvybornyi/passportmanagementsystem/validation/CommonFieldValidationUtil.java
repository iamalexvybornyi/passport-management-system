package com.iamalexvybornyi.passportmanagementsystem.validation;

import com.iamalexvybornyi.passportmanagementsystem.dto.passport.CreatePassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.person.CreatePersonDto;
import com.iamalexvybornyi.passportmanagementsystem.exception.BusinessValidationException;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import com.iamalexvybornyi.passportmanagementsystem.repository.PassportRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
@AllArgsConstructor
public class CommonFieldValidationUtil {

    @NonNull
    private final PassportRepository passportRepository;
    @NonNull
    private final MessageSource messageSource;

    public void verifyCreatePersonDtoForBusinessRequirements(@NonNull CreatePersonDto createPersonDto) {
        final Map<String, String> errors = new HashMap<>();
        if (LocalDate.parse(createPersonDto.getBirthDate(), DateTimeFormatter.ofPattern( "yyyy-MM-dd" ))
                .compareTo(LocalDate.now().minusYears(14)) >= 0) {
            errors.put("birthDate", messageSource.getMessage(
                    "passport.management.system.constraints.birth.date.validation.message",
                            null, Locale.ENGLISH));
        }

        if (errors.size() > 0) {
            throw new BusinessValidationException("Business Validation Error", errors);
        }
    }

    public void verifyCreatePassportDtoForBusinessRequirements(@Nullable String passportId,
                                                               @NonNull CreatePassportDto createPassportDto) {
        final Passport foundPassport = passportRepository.findByPassportNumber(createPassportDto.getPassportNumber());
        final Map<String, String> errors = new HashMap<>();
        if (foundPassport != null && !foundPassport.getId().equals(passportId)) {
            errors.put("passportNumber", messageSource.getMessage(
                    "passport.management.system.constraints.unique.passport.number.message",
                    null, Locale.ENGLISH));
        }

        if (errors.size() > 0) {
            throw new BusinessValidationException("Business Validation Error", errors);
        }
    }
}
