package com.iamalexvybornyi.passportmanagementsystem.controller.passport;

import com.iamalexvybornyi.passportmanagementsystem.BaseTest;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportWithPersonDto;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PassportControllerGetPassportTest extends BaseTest {

    @BeforeEach
    public void createPersonAndPassports() {
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
    public void getPassport_whenIdExistsAndIsValid_receiveOk() {
        Passport passport = passportRepository.findAll().iterator().next();
        Response response = getPassportByIdFromPassportEndpoint(passport.getId());
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassport_whenIdExistsAndIsValid_receiveCorrectPassportDto() {
        Person person = personRepository.findAll().iterator().next();
        Passport passport = person.getPassports().stream().findAny().orElse(null);
        PassportWithPersonDto expectedPassport = passportConverter.passportToPassportWithPersonDto(passport);
        expectedPassport.setPerson(personConverter.personToPersonDto(person));
        Response response = getPassportByIdFromPassportEndpoint(passport.getId());
        PassportWithPersonDto actualPassport = extractDataFromResponse(response, PassportWithPersonDto.class);
        assertThat(actualPassport).isEqualTo(expectedPassport);
    }

    @Test
    public void getPassport_whenIdDoesNotExistButIsValid_receiveNotFound() {
        Response response = getPassportByIdFromPassportEndpoint(0L);
        verifyResponseStatusCode(response, HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getPassport_whenIdIsNotValid_receiveBadRequest() {
        Response response = getPassportByIdFromPassportEndpoint("invalid_id");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }
}
