package com.iamalexvybornyi.passportmanagementsystem.controller.person;

import com.iamalexvybornyi.passportmanagementsystem.BaseTest;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportDto;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Status;
import com.iamalexvybornyi.passportmanagementsystem.service.PassportService;
import com.iamalexvybornyi.passportmanagementsystem.service.PersonService;
import io.restassured.response.Response;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonControllerGetPassportsTest extends BaseTest {

    @NonNull
    private final PersonService personService;
    @NonNull
    private final PassportService passportService;

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
        final Person person = new Person();
        person.setId(idGeneratorUtil.generatePersonId());
        person.setName("Some Name");
        person.setBirthCountry("Country");
        person.setBirthDate(LocalDate.of(1990, 1, 1));
        personRepository.save(person);
        personService.addPersonPassport(person.getId(), getValidCreatePassportDto(Status.ACTIVE));
        personService.addPersonPassport(person.getId(), getValidCreatePassportDto(Status.ACTIVE));
        personService.addPersonPassport(person.getId(), getValidCreatePassportDto(Status.ACTIVE));
        personService.addPersonPassport(person.getId(), getValidCreatePassportDto(Status.INACTIVE));
        personService.addPersonPassport(person.getId(), getValidCreatePassportDto(Status.INACTIVE));
        personService.addPersonPassport(person.getId(), getValidCreatePassportDto(Status.INACTIVE));
    }

    @Test
    public void getPassports_whenPassportsExist_receiveOk() {
        final Person person = personRepository.findAll().iterator().next();
        final Response response = getPassportsFromPassportEndpointByPersonId(person.getId());
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassports_whenPassportsExist_receiveExpectedPassports() {
        final Person person = personRepository.findAll().iterator().next();
        final List<PassportDto> expectedListOfPassports = passportRepository.findByPersonId(person.getId())
                .stream()
                .filter(passport -> passport.getStatus().equals(Status.ACTIVE))
                .map(passportConverter::passportToPassportDto)
                .toList();
        final Response response = getPassportsFromPassportEndpointByPersonId(person.getId());
        final List<PassportDto> actualListOfPassports = Arrays.asList(extractDataFromResponse(response, PassportDto[].class));
        assertThat(actualListOfPassports).isEqualTo(expectedListOfPassports);
    }

    @Test
    public void getPassports_whenPassportsDoNotExist_receiveOk() {
        passportRepository.deleteAll();
        final Person person = personRepository.findAll().iterator().next();
        final Response response = getPassportsFromPassportEndpointByPersonId(person.getId());
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassports_whenPassportsDoNotExist_receiveExpectedPassports() {
        passportRepository.deleteAll();
        final Person person = personRepository.findAll().iterator().next();
        final List<PassportDto> expectedListOfPassports = passportRepository.findByPersonId(person.getId())
                .stream()
                .map(passportConverter::passportToPassportDto)
                .toList();
        final Response response = getPassportsFromPassportEndpointByPersonId(person.getId());
        final List<PassportDto> actualListOfPassports = Arrays.asList(extractDataFromResponse(response, PassportDto[].class));
        assertThat(actualListOfPassports).isEqualTo(expectedListOfPassports);
    }

    @Test
    public void getPassports_whenPassportsExistAndStatusIsInactive_receiveOk() {
        final Person person = personRepository.findAll().iterator().next();
        final Response response = getPassportsFromPassportEndpointByPersonId(person.getId(), "status=INACTIVE");
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassports_whenPassportsExistAndStatusIsInactive_receiveExpectedPassports() {
        final Person person = personRepository.findAll().iterator().next();
        final List<PassportDto> expectedListOfPassports = passportRepository.findByPersonId(person.getId())
                .stream()
                .filter(passport -> passport.getStatus().equals(Status.INACTIVE))
                .map(passportConverter::passportToPassportDto)
                .toList();
        final Response response = getPassportsFromPassportEndpointByPersonId(person.getId(), "status=INACTIVE");
        final List<PassportDto> actualListOfPassports = Arrays.asList(extractDataFromResponse(response, PassportDto[].class));
        assertThat(actualListOfPassports).isEqualTo(expectedListOfPassports);
    }

    @Test
    public void getPassports_whenPassportsExistAndStatusIsActive_receiveOk() {
        final Person person = personRepository.findAll().iterator().next();
        final Response response = getPassportsFromPassportEndpointByPersonId(person.getId(), "status=ACTIVE");
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassports_whenPassportsExistAndStatusIsActive_receiveExpectedPassports() {
        final Person person = personRepository.findAll().iterator().next();
        passportService.deactivatePassport(
                Objects.requireNonNull(passportRepository.findByPersonId(person.getId()).stream().findFirst()
                        .orElse(null)).getId());
        final List<PassportDto> expectedListOfPassports = passportRepository.findByPersonId(person.getId())
                .stream()
                .filter(passport -> passport.getStatus().equals(Status.ACTIVE))
                .map(passportConverter::passportToPassportDto)
                .toList();
        final Response response = getPassportsFromPassportEndpointByPersonId(person.getId(), "status=ACTIVE");
        final List<PassportDto> actualListOfPassports = Arrays.asList(extractDataFromResponse(response, PassportDto[].class));
        assertThat(actualListOfPassports).isEqualTo(expectedListOfPassports);
    }

    @Test
    public void getPassports_whenStatusIsInvalid_receiveBadRequest() {
        final Person person = personRepository.findAll().iterator().next();
        final Response response = getPassportsFromPassportEndpointByPersonId(person.getId(), "status=invalid_value");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void getPassports_whenPersonIdDoesNotExistButIsValid_receiveNotFound() {
        final Response response = getPassportsFromPassportEndpointByPersonId(NON_EXISTING_ID);
        verifyResponseStatusCode(response, HttpStatus.NOT_FOUND.value());
    }
}
