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

    @Mapping(target = "givenDate", dateFormat = "yyyy-MM-dd")
    @NonNull
    CreatePassportDto passportToCreatePassportDto(@NonNull Passport passport);

    @Mapping(target = "givenDate", dateFormat = "yyyy-MM-dd")
    @NonNull
    PassportDto passportToPassportDto(@NonNull Passport passport);

    @Mapping(target = "givenDate", dateFormat = "yyyy-MM-dd")
    @NonNull
    PassportWithPersonDto passportToPassportWithPersonDto(@NonNull Passport passport);

    @Mapping(target = "givenDate", dateFormat = "yyyy-MM-dd")
    @NonNull
    Passport createPassportDtoToPassport(@NonNull CreatePassportDto createPassportDto, @NonNull String personId);
}
