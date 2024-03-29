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
    public void deactivatePassport_whenPassportExistsAndIsActive_receiveNoContent() {
        final Passport passport = passportRepository.findAll().iterator().next();
        final Response response = deactivatePassportByIdFromPassportEndpoint(passport.getId());
        verifyResponseStatusCode(response, HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void deactivatePassport_whenPassportExistsAndIsActive_statusIsUpdatedInDatasource() {
        final Passport passport = passportRepository.findAll().iterator().next();
        deactivatePassportByIdFromPassportEndpoint(passport.getId());
        final Passport updatedPassport = passportRepository.findById(passport.getId());
        assertThat(updatedPassport.getStatus()).isEqualTo(Status.INACTIVE);
    }

    @Test
    public void deactivatePassport_whenPassportExistsAndIsInactive_receiveNoContent() {
        final Passport passport = passportRepository.findAll().iterator().next();
        passport.setStatus(Status.INACTIVE);
        passportRepository.save(passport);
        final Response response = deactivatePassportByIdFromPassportEndpoint(passport.getId());
        verifyResponseStatusCode(response, HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void deactivatePassport_whenPassportExistsAndIsInactive_statusIsNotUpdatedInDatasource() {
        final Passport passport = passportRepository.findAll().iterator().next();
        passport.setStatus(Status.INACTIVE);
        passportRepository.save(passport);
        deactivatePassportByIdFromPassportEndpoint(passport.getId());
        final Passport updatedPassport = passportRepository.findById(passport.getId());
        assertThat(updatedPassport.getStatus()).isEqualTo(Status.INACTIVE);
    }

    @Test
    public void deactivatePassport_whenPassportDoesNotExist_receiveNotFound() {
        final Response response = deactivatePassportByIdFromPassportEndpoint(NON_EXISTING_ID);
        verifyResponseStatusCode(response, HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void deactivatePassport_whenPassportDoesNotExist_receiveRelatedError() {
        final Response response = deactivatePassportByIdFromPassportEndpoint(NON_EXISTING_ID);
        final ApiError apiError = extractDataFromResponse(response, ApiError.class);
        assertThat(apiError.getMessage()).isEqualTo("Passport is not found");
    }
}
