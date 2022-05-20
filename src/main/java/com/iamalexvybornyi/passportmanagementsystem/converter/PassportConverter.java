package com.iamalexvybornyi.passportmanagementsystem.converter;

import com.iamalexvybornyi.passportmanagementsystem.dto.passport.CreatePassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportWithPersonDto;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import org.mapstruct.Mapper;

@Mapper
public interface PassportConverter {

    CreatePassportDto passportToCreatePassportDto(Passport passport);

    PassportDto passportToPassportDto(Passport passport);

    PassportWithPersonDto passportToPassportWithPersonDto(Passport passport);

    Passport createPassportDtoToPassport(CreatePassportDto createPassportDto);
}
