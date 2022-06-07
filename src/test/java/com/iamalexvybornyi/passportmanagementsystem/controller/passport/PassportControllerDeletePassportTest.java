package com.iamalexvybornyi.passportmanagementsystem.controller.passport;

import com.iamalexvybornyi.passportmanagementsystem.BaseTest;
import com.iamalexvybornyi.passportmanagementsystem.model.ApiError;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PassportControllerDeletePassportTest extends BaseTest {

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
    public void deletePassport_whenPassportExists_receiveNoContent() {
        Passport passport = passportRepository.findAll().iterator().next();
        Response response = deletePassportByIdFromPassportEndpoint(passport.getId());
        verifyResponseStatusCode(response, HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void deletePassport_whenPassportExists_passportIsDeletedFromDatasource() {
        Passport passport = passportRepository.findAll().iterator().next();
        deletePassportByIdFromPassportEndpoint(passport.getId());
        Passport updatedPassport = passportRepository.findById(passport.getId());
        assertThat(updatedPassport).isEqualTo(null);
    }

    @Test
    public void deletePassport_whenPassportDoesNotExist_receiveNotFound() {
        Response response = deletePassportByIdFromPassportEndpoint(0L);
        verifyResponseStatusCode(response, HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void deletePassport_whenPassportDoesNotExist_receiveRelatedError() {
        Response response = deletePassportByIdFromPassportEndpoint(0L);
        ApiError apiError = extractDataFromResponse(response, ApiError.class);
        assertThat(apiError.getMessage()).isEqualTo("Passport is not found");
    }

    @Test
    public void deletePassport_whenPassportIdIsInvalid_receiveBadRequest() {
        Response response = deletePassportByIdFromPassportEndpoint("invalid_id");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void deletePassport_whenPassportIdIsInvalid_receiveRelatedError() {
        Response response = deletePassportByIdFromPassportEndpoint("invalid_id");
        ApiError apiError = extractDataFromResponse(response, ApiError.class);
        assertThat(apiError.getMessage()).isEqualTo("Invalid input data");
    }
}
