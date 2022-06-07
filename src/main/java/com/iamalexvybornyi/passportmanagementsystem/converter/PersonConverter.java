package com.iamalexvybornyi.passportmanagementsystem.converter;

import com.iamalexvybornyi.passportmanagementsystem.dto.person.CreatePersonDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.person.PersonDto;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PersonConverter {

    @Mapping(target = "birthDate", dateFormat = "dd-MM-yyyy")
    CreatePersonDto personToCreatePersonDto(Person person);

    @Mapping(target = "birthDate", dateFormat = "dd-MM-yyyy")
    PersonDto personToPersonDto(Person person);

    @Mapping(target = "birthDate", dateFormat = "dd-MM-yyyy")
    Person createPersonDtoToPerson(CreatePersonDto personDto);

    @Mapping(target = "birthDate", dateFormat = "dd-MM-yyyy")
    Person personDtoToPerson(PersonDto personDto);
}
