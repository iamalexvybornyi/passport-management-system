package com.iamalexvybornyi.passportmanagementsystem.converter;

import com.iamalexvybornyi.passportmanagementsystem.dto.passport.CreatePassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportWithPersonDto;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PassportConverter {

    @Mapping(target = "givenDate", dateFormat = "dd-MM-yyyy")
    CreatePassportDto passportToCreatePassportDto(Passport passport);

    @Mapping(target = "givenDate", dateFormat = "dd-MM-yyyy")
    PassportDto passportToPassportDto(Passport passport);

    @Mapping(target = "givenDate", dateFormat = "dd-MM-yyyy")
    PassportWithPersonDto passportToPassportWithPersonDto(Passport passport);

    @Mapping(target = "givenDate", dateFormat = "dd-MM-yyyy")
    Passport createPassportDtoToPassport(CreatePassportDto createPassportDto);
}
