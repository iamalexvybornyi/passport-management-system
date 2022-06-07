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

public class PassportControllerDeactivatePassportTest extends BaseTest {

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
    public void deactivatePassport_whenPassportExistsAndIsActive_receiveNoContent() {
        Passport passport = passportRepository.findAll().iterator().next();
        Response response = deactivatePassportByIdFromPassportEndpoint(passport.getId());
        verifyResponseStatusCode(response, HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void deactivatePassport_whenPassportExistsAndIsActive_statusIsUpdatedInDatasource() {
        Passport passport = passportRepository.findAll().iterator().next();
        deactivatePassportByIdFromPassportEndpoint(passport.getId());
        Passport updatedPassport = passportRepository.findById(passport.getId());
        assertThat(updatedPassport.getStatus()).isEqualTo(Status.INACTIVE);
    }

    @Test
    public void deactivatePassport_whenPassportExistsAndIsInactive_receiveNoContent() {
        Passport passport = passportRepository.findAll().iterator().next();
        passport.setStatus(Status.INACTIVE);
        passportRepository.save(passport);
        Response response = deactivatePassportByIdFromPassportEndpoint(passport.getId());
        verifyResponseStatusCode(response, HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void deactivatePassport_whenPassportExistsAndIsInactive_statusIsNotUpdatedInDatasource() {
        Passport passport = passportRepository.findAll().iterator().next();
        passport.setStatus(Status.INACTIVE);
        passportRepository.save(passport);
        deactivatePassportByIdFromPassportEndpoint(passport.getId());
        Passport updatedPassport = passportRepository.findById(passport.getId());
        assertThat(updatedPassport.getStatus()).isEqualTo(Status.INACTIVE);
    }

    @Test
    public void deactivatePassport_whenPassportDoesNotExist_receiveNotFound() {
        Response response = deactivatePassportByIdFromPassportEndpoint(0L);
        verifyResponseStatusCode(response, HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void deactivatePassport_whenPassportDoesNotExist_receiveRelatedError() {
        Response response = deactivatePassportByIdFromPassportEndpoint(0L);
        ApiError apiError = extractDataFromResponse(response, ApiError.class);
        assertThat(apiError.getMessage()).isEqualTo("Passport is not found");
    }

    @Test
    public void deactivatePassport_whenPassportIdIsInvalid_receiveBadRequest() {
        Response response = deactivatePassportByIdFromPassportEndpoint("invalid_id");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void deactivatePassport_whenPassportIdIsInvalid_receiveRelatedError() {
        Response response = deactivatePassportByIdFromPassportEndpoint("invalid_id");
        ApiError apiError = extractDataFromResponse(response, ApiError.class);
        assertThat(apiError.getMessage()).isEqualTo("Invalid input data");
    }
}
