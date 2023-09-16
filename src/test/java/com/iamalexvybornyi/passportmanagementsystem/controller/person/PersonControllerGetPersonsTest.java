package com.iamalexvybornyi.passportmanagementsystem.controller.person;

import com.iamalexvybornyi.passportmanagementsystem.BaseTest;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.CreatePassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.person.PersonDto;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Status;
import com.iamalexvybornyi.passportmanagementsystem.service.PersonService;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonControllerGetPersonsTest extends BaseTest {

    private final PersonService personService;

    @Autowired
    public PersonControllerGetPersonsTest(
            PersonService personService
    ) {
        this.personService = personService;
    }

    @BeforeEach
    public void createValidPerson() {
        for (int i = 1; i <= 10; i++) {
            final Person person = new Person();
            person.setId(idGeneratorUtil.generatePersonId());
            person.setName("Some Name " + i);
            person.setBirthCountry("Country " + i);
            person.setBirthDate(LocalDate.of(1990, 1, 1));
            personRepository.save(person);
        }
    }

    @Test
    public void getPersons_whenPersonsExist_receiveOk() {
        final Response response = getPersonsFromPersonEndpoint();
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPersons_whenPersonsDoNotExist_receiveOk() {
        personRepository.deleteAll();
        final Response response = getPersonsFromPersonEndpoint();
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPersons_whenPersonsExist_receiveCorrectListOfPersonDto() {
        this.getPersonListAndVerifyItsContents();
    }

    @Test
    public void getPersons_whenPersonsDoNotExist_receiveEmptyListOfPersonDto() {
        personRepository.deleteAll();
        this.getPersonListAndVerifyItsContents();
    }

    @Test
    public void getPersons_whenPassportNumberIsUsedAndExists_receiveOk() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePassportDto validPassport = getValidCreatePassportDto(Status.ACTIVE);
        personService.addPersonPassport(person.getId(), validPassport);
        final Response response = getPersonsFromPersonEndpoint("passportNumber=" + validPassport.getPassportNumber());
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPersons_whenPassportNumberIsUsedAndExists_receiveCorrectListOfPersonDto() {
        final Person person = personRepository.findAll().iterator().next();
        final List<PersonDto> expectedListOfPersons = List.of(personConverter.personToPersonDto(person));
        final CreatePassportDto validPassport = getValidCreatePassportDto(Status.ACTIVE);
        personService.addPersonPassport(person.getId(), validPassport);
        final Response response = getPersonsFromPersonEndpoint("passportNumber=" + validPassport.getPassportNumber());
        final List<PersonDto> actualListOfPersons = List.of(extractDataFromResponse(response, PersonDto[].class));
        assertThat(actualListOfPersons).isEqualTo(expectedListOfPersons);
    }

    @Test
    public void getPersons_whenPassportNumberIsUsedAndDoesNotExist_receiveOk() {
        final Response response = getPersonsFromPersonEndpoint("passportNumber=" +
                RandomStringUtils.randomAlphanumeric(10));
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPersons_whenPassportNumberIsUsedAndAndDoesNotExist_receiveEmptyListOfPersonDto() {
        final Response response = getPersonsFromPersonEndpoint("passportNumber=" +
                RandomStringUtils.randomAlphanumeric(10));
        final List<PersonDto> actualListOfPersons = List.of(extractDataFromResponse(response, PersonDto[].class));
        final List<PersonDto> expectedListOfPersons = new ArrayList<>();
        assertThat(actualListOfPersons).isEqualTo(expectedListOfPersons);
    }

    private void getPersonListAndVerifyItsContents() {
        final List<PersonDto> expectedPersonDtoList = new ArrayList<>();
        personRepository.findAll()
                .forEach(person -> expectedPersonDtoList.add(personConverter.personToPersonDto(person)));
        final List<PersonDto> actualPersonDtoList = Arrays.asList(extractDataFromResponse(getPersonsFromPersonEndpoint(),
                PersonDto[].class));
        assertThat(actualPersonDtoList).isEqualTo(expectedPersonDtoList);
    }
}
