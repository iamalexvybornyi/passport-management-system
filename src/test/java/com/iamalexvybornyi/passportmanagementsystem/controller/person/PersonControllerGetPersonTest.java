package com.iamalexvybornyi.passportmanagementsystem.controller.person;

import com.iamalexvybornyi.passportmanagementsystem.BaseTest;
import com.iamalexvybornyi.passportmanagementsystem.converter.PassportConverter;
import com.iamalexvybornyi.passportmanagementsystem.converter.PersonConverter;
import com.iamalexvybornyi.passportmanagementsystem.dto.person.PersonDto;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonControllerGetPersonTest extends BaseTest {

    private PersonConverter personConverter;

    private PassportConverter passportConverter;

    @Autowired
    public PersonControllerGetPersonTest(
            PersonConverter personConverter,
            PassportConverter passportConverter
    ) {
        this.personConverter = personConverter;
        this.passportConverter = passportConverter;
    }

    @BeforeEach
    public void createValidPerson() {
        for (int i = 1; i <= 10; i++) {
            Person person = new Person();
            person.setName("Some Name " + i);
            person.setBirthCountry("Country " + i);
            person.setBirthDate(LocalDate.of(1990, 1, 1));
            personRepository.save(person);
        }
    }

    @Test
    public void getPerson_whenIdExistsAndIsValid_receiveOk() {
        Person person = personRepository.findAll().iterator().next();
        Response response = getPersonByIdFromPersonEndpoint(person.getId());
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPerson_whenIdExistsAndIsValid_receiveCorrectPersonDto() {
        Person person = personRepository.findAll().iterator().next();
        PersonDto expectedPersonDto = personConverter.personToPersonDto(person);
        PersonDto actualPersonDto = extractDataFromResponse(getPersonByIdFromPersonEndpoint(person.getId()),
                PersonDto.class);
        assertThat(actualPersonDto).isEqualTo(expectedPersonDto);
    }

    @Test
    public void getPerson_whenIdDoesNotExistButIsValid_receiveNotFound() {
        Response response = getPersonByIdFromPersonEndpoint(0L);
        verifyResponseStatusCode(response, HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getPerson_whenIdIsNotValid_receiveBadRequest() {
        Response response = getPersonByIdFromPersonEndpoint("invalid_id");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }
}
