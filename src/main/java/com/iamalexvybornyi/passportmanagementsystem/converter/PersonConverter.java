package com.iamalexvybornyi.passportmanagementsystem.converter;

import com.iamalexvybornyi.passportmanagementsystem.dto.person.CreatePersonDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.person.PersonDto;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import org.mapstruct.Mapper;

@Mapper
public interface PersonConverter {

    CreatePersonDto personToCreatePersonDto(Person person);

    PersonDto personToPersonDto(Person person);

    Person createPersonDtoToPerson(CreatePersonDto personDto);

    Person personDtoToPerson(PersonDto personDto);
}
