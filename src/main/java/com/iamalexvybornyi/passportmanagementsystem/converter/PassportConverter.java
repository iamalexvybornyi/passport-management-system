package com.iamalexvybornyi.passportmanagementsystem.converter;

import com.iamalexvybornyi.passportmanagementsystem.dto.passport.CreatePassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportWithPersonDto;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PassportConverter {

    @Mapping(target = "givenDate", dateFormat = "dd-MM-yyyy")
    @NonNull
    CreatePassportDto passportToCreatePassportDto(@NonNull Passport passport);

    @Mapping(target = "givenDate", dateFormat = "dd-MM-yyyy")
    @NonNull
    PassportDto passportToPassportDto(@NonNull Passport passport);

    @Mapping(target = "givenDate", dateFormat = "dd-MM-yyyy")
    @NonNull
    PassportWithPersonDto passportToPassportWithPersonDto(@NonNull Passport passport);

    @Mapping(target = "givenDate", dateFormat = "dd-MM-yyyy")
    @NonNull
    Passport createPassportDtoToPassport(@NonNull CreatePassportDto createPassportDto);
}
