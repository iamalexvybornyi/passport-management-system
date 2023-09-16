package com.iamalexvybornyi.passportmanagementsystem.controller.passport;

import com.iamalexvybornyi.passportmanagementsystem.BaseTest;
import com.iamalexvybornyi.passportmanagementsystem.model.ApiError;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Status;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PassportControllerDeletePassportTest extends BaseTest {

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
    public void deletePassport_whenPassportExists_receiveNoContent() {
        final Passport passport = passportRepository.findAll().iterator().next();
        final Response response = deletePassportByIdFromPassportEndpoint(passport.getId());
        verifyResponseStatusCode(response, HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void deletePassport_whenPassportExists_passportIsDeletedFromDatasource() {
        final Passport passport = passportRepository.findAll().iterator().next();
        deletePassportByIdFromPassportEndpoint(passport.getId());
        final Passport updatedPassport = passportRepository.findById(passport.getId());
        assertThat(updatedPassport).isEqualTo(null);
    }

    @Test
    public void deletePassport_whenPassportDoesNotExist_receiveNotFound() {
        final Response response = deletePassportByIdFromPassportEndpoint(NON_EXISTING_ID);
        verifyResponseStatusCode(response, HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void deletePassport_whenPassportDoesNotExist_receiveRelatedError() {
        final Response response = deletePassportByIdFromPassportEndpoint(NON_EXISTING_ID);
        final ApiError apiError = extractDataFromResponse(response, ApiError.class);
        assertThat(apiError.getMessage()).isEqualTo("Passport is not found");
    }
}
