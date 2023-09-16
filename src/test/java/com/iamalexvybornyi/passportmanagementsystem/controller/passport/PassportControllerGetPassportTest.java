package com.iamalexvybornyi.passportmanagementsystem.controller.passport;

import com.iamalexvybornyi.passportmanagementsystem.BaseTest;
import com.iamalexvybornyi.passportmanagementsystem.controller.exception.EntityNotFoundException;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportWithPersonDto;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Status;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PassportControllerGetPassportTest extends BaseTest {

    @BeforeEach
    public void createPersonAndPassports() {
        final Person person = new Person();
        person.setId(idGeneratorUtil.generatePersonId());
        person.setName("Some Name");
        person.setBirthCountry("Country");
        person.setBirthDate(LocalDate.of(1990, 1, 1));
        personRepository.save(person);
        personService.addPersonPassport(person.getId(), getValidCreatePassportDto(Status.ACTIVE));
        personService.addPersonPassport(person.getId(), getValidCreatePassportDto(Status.ACTIVE));
        personService.addPersonPassport(person.getId(), getValidCreatePassportDto(Status.ACTIVE));
    }

    @Test
    public void getPassport_whenIdExistsAndIsValid_receiveOk() {
        final Passport passport = passportRepository.findAll().iterator().next();
        final Response response = getPassportByIdFromPassportEndpoint(passport.getId());
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassport_whenIdExistsAndIsValid_receiveCorrectPassportDto() {
        final Person person = personRepository.findAll().iterator().next();
        final Passport passport = person.getPassports().stream().findAny().orElseThrow(EntityNotFoundException::new);
        final PassportWithPersonDto expectedPassport = passportConverter.passportToPassportWithPersonDto(passport);
        expectedPassport.setPerson(personConverter.personToPersonDto(person));
        final Response response = getPassportByIdFromPassportEndpoint(passport.getId());
        final PassportWithPersonDto actualPassport = extractDataFromResponse(response, PassportWithPersonDto.class);
        assertThat(actualPassport).isEqualTo(expectedPassport);
    }

    @Test
    public void getPassport_whenIdDoesNotExistButIsValid_receiveNotFound() {
        final Response response = getPassportByIdFromPassportEndpoint(NON_EXISTING_ID);
        verifyResponseStatusCode(response, HttpStatus.NOT_FOUND.value());
    }
}
