package com.iamalexvybornyi.passportmanagementsystem.controller.person;

import com.iamalexvybornyi.passportmanagementsystem.BaseTest;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportDto;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Status;
import com.iamalexvybornyi.passportmanagementsystem.service.PassportService;
import com.iamalexvybornyi.passportmanagementsystem.service.PersonService;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonControllerGetPassportsTest extends BaseTest {

    private PersonService personService;
    private PassportService passportService;

    @Autowired
    public PersonControllerGetPassportsTest(
            PersonService personService,
            PassportService passportService
    ) {
        this.personService = personService;
        this.passportService = passportService;
    }

    @BeforeEach
    public void createValidPerson() {
        Person person = new Person();
        person.setName("Some Name");
        person.setBirthCountry("Country");
        person.setBirthDate(LocalDate.of(1990, 1, 1));
        personRepository.save(person);
        personService.addPersonPassport(person.getId(), getValidCreatePassportDto());
        personService.addPersonPassport(person.getId(), getValidCreatePassportDto());
        personService.addPersonPassport(person.getId(), getValidCreatePassportDto());
    }

    @Test
    public void getPassports_whenPassportsExist_receiveOk() {
        Person person = personRepository.findAll().iterator().next();
        Response response = getPassportsFromPassportEndpointByPersonId(person.getId());
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassports_whenPassportsExist_receiveExpectedPassports() {
        Person person = personRepository.findAll().iterator().next();
        List<PassportDto> expectedListOfPassports = new ArrayList<>();
        person.getPassports().forEach(passport ->
                expectedListOfPassports.add(passportConverter.passportToPassportDto(passport)));
        Response response = getPassportsFromPassportEndpointByPersonId(person.getId());
        List<PassportDto> actualListOfPassports = Arrays.asList(extractDataFromResponse(response, PassportDto[].class));
        assertThat(actualListOfPassports).isEqualTo(expectedListOfPassports);
    }

    @Test
    public void getPassports_whenPassportsDoNotExist_receiveOk() {
        passportRepository.deleteAll();
        Person person = personRepository.findAll().iterator().next();
        Response response = getPassportsFromPassportEndpointByPersonId(person.getId());
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassports_whenPassportsDoNotExist_receiveExpectedPassports() {
        passportRepository.deleteAll();
        Person person = personRepository.findAll().iterator().next();
        List<PassportDto> expectedListOfPassports = new ArrayList<>();
        person.getPassports().forEach(passport ->
                expectedListOfPassports.add(passportConverter.passportToPassportDto(passport)));
        Response response = getPassportsFromPassportEndpointByPersonId(person.getId());
        List<PassportDto> actualListOfPassports = Arrays.asList(extractDataFromResponse(response, PassportDto[].class));
        assertThat(actualListOfPassports).isEqualTo(expectedListOfPassports);
    }

    @Test
    public void getPassports_whenPassportsExistAndActiveOnlyFlagIsFalse_receiveOk() {
        Person person = personRepository.findAll().iterator().next();
        Response response = getPassportsFromPassportEndpointByPersonId(person.getId(), "activeOnly=false");
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassports_whenPassportsExistAndActiveOnlyFlagIsFalse_receiveExpectedPassports() {
        Person person = personRepository.findAll().iterator().next();
        List<PassportDto> expectedListOfPassports = new ArrayList<>();
        person.getPassports().forEach(passport ->
                expectedListOfPassports.add(passportConverter.passportToPassportDto(passport)));
        Response response = getPassportsFromPassportEndpointByPersonId(person.getId(), "activeOnly=false");
        List<PassportDto> actualListOfPassports = Arrays.asList(extractDataFromResponse(response, PassportDto[].class));
        assertThat(actualListOfPassports).isEqualTo(expectedListOfPassports);
    }

    @Test
    public void getPassports_whenPassportsExistAndActiveOnlyFlagIsTrue_receiveOk() {
        Person person = personRepository.findAll().iterator().next();
        Response response = getPassportsFromPassportEndpointByPersonId(person.getId(), "activeOnly=true");
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassports_whenPassportsExistAndActiveOnlyFlagIsTrue_receiveExpectedPassports() {
        Person person = personRepository.findAll().iterator().next();
        passportService.deactivatePassport(person.getPassports().stream().findAny().orElse(null).getId());
        List<PassportDto> expectedListOfPassports = new ArrayList<>();
        person.getPassports().forEach(passport -> {
            if (passport.getStatus().equals(Status.ACTIVE)) {
                expectedListOfPassports.add(passportConverter.passportToPassportDto(passport));
            }
        });
        Response response = getPassportsFromPassportEndpointByPersonId(person.getId(), "activeOnly=true");
        List<PassportDto> actualListOfPassports = Arrays.asList(extractDataFromResponse(response, PassportDto[].class));
        assertThat(actualListOfPassports).isEqualTo(expectedListOfPassports);
    }

    @Test
    public void getPassports_whenActiveOnlyFlagValueIsInvalid_receiveBadRequest() {
        Person person = personRepository.findAll().iterator().next();
        Response response = getPassportsFromPassportEndpointByPersonId(person.getId(), "activeOnly=invalid_value");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void getPassports_whenPersonIdDoesNotExistButIsValid_receiveNotFound() {
        Response response = getPassportsFromPassportEndpointByPersonId(0L);
        verifyResponseStatusCode(response, HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getPassports_whenPersonIdIsNotValid_receiveBadRequest() {
        Response response = getPassportsFromPassportEndpointByPersonId("invalid_id");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }
}
