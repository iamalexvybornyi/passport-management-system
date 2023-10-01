package com.iamalexvybornyi.passportmanagementsystem.converter;

import com.iamalexvybornyi.passportmanagementsystem.dto.person.CreatePersonDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.person.PersonDto;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PersonConverter {

    @Mapping(target = "birthDate", dateFormat = "yyyy-MM-dd")
    @NonNull
    CreatePersonDto personToCreatePersonDto(@NonNull Person person);

    @Mapping(target = "birthDate", dateFormat = "yyyy-MM-dd")
    @NonNull
    PersonDto personToPersonDto(@NonNull Person person);

    @Mapping(target = "birthDate", dateFormat = "yyyy-MM-dd")
    @NonNull
    Person createPersonDtoToPerson(@NonNull CreatePersonDto personDto);

    @Mapping(target = "birthDate", dateFormat = "yyyy-MM-dd")
    @NonNull
    Person personDtoToPerson(@NonNull PersonDto personDto);
}
