package com.iamalexvybornyi.passportmanagementsystem.controller.passport;

import com.iamalexvybornyi.passportmanagementsystem.BaseTest;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.CreatePassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportDto;
import com.iamalexvybornyi.passportmanagementsystem.model.ApiError;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Passport;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.PassportType;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Status;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

public class PassportControllerUpdatePassportTest extends BaseTest {

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
    }

    @Test
    public void updatePassport_whenPassportIsValid_receiveOk() {
        final Passport passport = passportRepository.findAll().iterator().next();
        final CreatePassportDto createPassportDto = passportConverter.passportToCreatePassportDto(passport);
        createPassportDto.setPassportNumber(RandomStringUtils.randomAlphanumeric(10));
        final Response response = sendCreatePassportDtoToPassportEndpointForUpdate(passport.getId(), createPassportDto);
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void updatePassport_whenPassportIsValid_receiveCorrectPassportDto() {
        final Passport passport = passportRepository.findAll().iterator().next();
        final CreatePassportDto createPassportDto = passportConverter.passportToCreatePassportDto(passport);
        createPassportDto.setPassportNumber(RandomStringUtils.randomAlphanumeric(10));
        createPassportDto.setPassportType(PassportType.INTERNAL.toString());
        createPassportDto.setDepartmentCode("222-222");
        createPassportDto.setStatus(Status.INACTIVE.toString());
        createPassportDto.setGivenDate(LocalDate.now().minusDays(7).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        final Response response = sendCreatePassportDtoToPassportEndpointForUpdate(passport.getId(), createPassportDto);
        final PassportDto actualPassport = extractDataFromResponse(response, PassportDto.class);
        final PassportDto expectedPassport = passportConverter.passportToPassportDto(passportRepository.findById(passport.getId()));
        assertThat(actualPassport).isEqualTo(expectedPassport);
    }

    // Passport Number
    @Test
    public void updatePassport_whenPassportNumberHasLessCharsThanRequired_receiveBadRequest() {
        final Response response = this.findPassportAndSetItsNumberAndAttemptToUpdatePassport(
                RandomStringUtils.randomAlphanumeric(4));
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePassport_whenPassportNumberHasLessCharsThanRequired_receiveRelatedMessage() {
        final Response response = this.findPassportAndSetItsNumberAndAttemptToUpdatePassport(
                RandomStringUtils.randomAlphanumeric(4));
        extractAndVerifyApiErrorResponseForField(response, "passportNumber",
                "size must be between 5 and 20");
    }

    @Test
    public void updatePassport_whenPassportNumberHasMoreCharsThanRequired_receiveBadRequest() {
        final Response response = this.findPassportAndSetItsNumberAndAttemptToUpdatePassport(
                RandomStringUtils.randomAlphanumeric(21));
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePassport_whenPassportNumberHasMoreCharsThanRequired_receiveRelatedMessage() {
        final Response response = this.findPassportAndSetItsNumberAndAttemptToUpdatePassport(
                RandomStringUtils.randomAlphanumeric(21));
        extractAndVerifyApiErrorResponseForField(response, "passportNumber",
                "size must be between 5 and 20");
    }

    @Test
    public void updatePassport_whenPassportNumberIsNull_receiveBadRequest() {
        final Response response = this.findPassportAndSetItsNumberAndAttemptToUpdatePassport(null);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePassport_whenPassportNumberIsNull_receiveRelatedMessage() {
        final Response response = this.findPassportAndSetItsNumberAndAttemptToUpdatePassport(null);
        extractAndVerifyApiErrorResponseForField(response, "passportNumber", "must not be null");
    }

    @Test
    public void updatePassport_whenPassportNumberIsNotUnique_receiveUnprocessableEntity() {
        final Iterator<Passport> passportIterator = passportRepository.findAll().iterator();
        passportIterator.next();
        final Response response = this.findPassportAndSetItsNumberAndAttemptToUpdatePassport(
                passportIterator.next().getPassportNumber());
        verifyResponseStatusCode(response, HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void updatePassport_whenPassportNumberIsNotUnique_receiveRelatedMessage() {
        final Iterator<Passport> passportIterator = passportRepository.findAll().iterator();
        passportIterator.next();
        final Response response = this.findPassportAndSetItsNumberAndAttemptToUpdatePassport(
                passportIterator.next().getPassportNumber());
        extractAndVerifyApiErrorResponseForField(response, "passportNumber",
                propertyReaderUtil.getUniquePassportValidationMessage());
    }

    private Response findPassportAndSetItsNumberAndAttemptToUpdatePassport(String passportNumberValue) {
        final Passport passport = passportRepository.findAll().iterator().next();
        final CreatePassportDto passportForRequest = passportConverter.passportToCreatePassportDto(passport);
        passportForRequest.setPassportNumber(passportNumberValue);
        return sendCreatePassportDtoToPassportEndpointForUpdate(passport.getId(), passportForRequest);
    }

    // Given date
    @Test
    public void updatePassport_whenGivenDateHasInvalidFormat_receiveBadRequest() {
        final Response response = this.findPassportAndSetItsGivenDateAndAttemptToUpdatePassport("11-11-199");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePassport_whenGivenDateHasInvalidFormat_receiveRelatedMessage() {
        final Response response = this.findPassportAndSetItsGivenDateAndAttemptToUpdatePassport("11-11-199");
        extractAndVerifyApiErrorResponseForField(response, "givenDate",
                propertyReaderUtil.getGivenDateValidationMessage());
    }

    @Test
    public void updatePassport_whenGivenDateIsNull_receiveBadRequest() {
        final Response response = this.findPassportAndSetItsGivenDateAndAttemptToUpdatePassport(null);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePassport_whenGivenDateIsNull_receiveRelatedMessage() {
        final Response response = this.findPassportAndSetItsGivenDateAndAttemptToUpdatePassport(null);
        extractAndVerifyApiErrorResponseForField(response, "givenDate", "must not be null");
    }

    private Response findPassportAndSetItsGivenDateAndAttemptToUpdatePassport(String givenDate) {
        final Passport passport = passportRepository.findAll().iterator().next();
        final CreatePassportDto passportForRequest = passportConverter.passportToCreatePassportDto(passport);
        passportForRequest.setGivenDate(givenDate);
        return sendCreatePassportDtoToPassportEndpointForUpdate(passport.getId(), passportForRequest);
    }

    // Department code
    @Test
    public void updatePassport_whenDepartmentCodeHasInvalidFormat_receiveBadRequest() {
        final Response response = this.findPassportAndSetItsDepartmentCodeAndAttemptToUpdatePassport("111-22");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePassport_whenDepartmentCodeHasInvalidFormat_receiveRelatedMessage() {
        final Response response = this.findPassportAndSetItsDepartmentCodeAndAttemptToUpdatePassport("111-22");
        extractAndVerifyApiErrorResponseForField(response, "departmentCode",
                propertyReaderUtil.getDepartmentCodeValidationMessage());
    }

    @Test
    public void updatePassport_whenDepartmentCodeIsNull_receiveBadRequest() {
        final Response response = this.findPassportAndSetItsDepartmentCodeAndAttemptToUpdatePassport(null);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePassport_whenDepartmentCodeIsNull_receiveRelatedMessage() {
        final Response response = this.findPassportAndSetItsDepartmentCodeAndAttemptToUpdatePassport(null);
        extractAndVerifyApiErrorResponseForField(response, "departmentCode", "must not be null");
    }

    private Response findPassportAndSetItsDepartmentCodeAndAttemptToUpdatePassport(String departmentCode) {
        final Passport passport = passportRepository.findAll().iterator().next();
        final CreatePassportDto passportForRequest = passportConverter.passportToCreatePassportDto(passport);
        passportForRequest.setDepartmentCode(departmentCode);
        return sendCreatePassportDtoToPassportEndpointForUpdate(passport.getId(), passportForRequest);
    }

    // Passport Type
    @Test
    public void updatePassport_whenPassportTypeHasInvalidValue_receiveBadRequest() {
        final Response response = this.findPassportAndSetItsTypeAndAttemptToUpdatePassport("INVALID_STATUS");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePassport_whenPassportTypeHasInvalidValue_receiveRelatedMessage() {
        final Response response = this.findPassportAndSetItsTypeAndAttemptToUpdatePassport("INVALID_STATUS");
        extractAndVerifyApiErrorResponseForField(response, "passportType",
                propertyReaderUtil.getPassportTypeValidationMessage());
    }

    @Test
    public void updatePassport_whenPassportTypeIsNull_receiveBadRequest() {
        final Response response = this.findPassportAndSetItsTypeAndAttemptToUpdatePassport(null);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePassport_whenPassportTypeIsNull_receiveRelatedMessage() {
        final Response response = this.findPassportAndSetItsTypeAndAttemptToUpdatePassport(null);
        extractAndVerifyApiErrorResponseForField(response, "passportType", "must not be null");
    }

    private Response findPassportAndSetItsTypeAndAttemptToUpdatePassport(String passportType) {
        final Passport passport = passportRepository.findAll().iterator().next();
        final CreatePassportDto passportForRequest = passportConverter.passportToCreatePassportDto(passport);
        passportForRequest.setPassportType(passportType);
        return sendCreatePassportDtoToPassportEndpointForUpdate(passport.getId(), passportForRequest);
    }

    // Passport Type
    @Test
    public void updatePassport_whenPassportStatusHasInvalidValue_receiveBadRequest() {
        final Response response = this.findPassportAndSetItsStatusAndAttemptToUpdatePassport("INVALID_STATUS");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePassport_whenPassportStatusHasInvalidValue_receiveRelatedMessage() {
        final Response response = this.findPassportAndSetItsStatusAndAttemptToUpdatePassport("INVALID_STATUS");
        extractAndVerifyApiErrorResponseForField(response, "status",
                propertyReaderUtil.getPassportStatusValidationMessage());
    }

    @Test
    public void updatePassport_whenPassportStatusIsNull_receiveBadRequest() {
        final Response response = this.findPassportAndSetItsStatusAndAttemptToUpdatePassport(null);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePassport_whenPassportStatusIsNull_receiveRelatedMessage() {
        final Response response = this.findPassportAndSetItsStatusAndAttemptToUpdatePassport(null);
        extractAndVerifyApiErrorResponseForField(response, "status", "must not be null");
    }

    private Response findPassportAndSetItsStatusAndAttemptToUpdatePassport(String passportStatus) {
        final Passport passport = passportRepository.findAll().iterator().next();
        final CreatePassportDto passportForRequest = passportConverter.passportToCreatePassportDto(passport);
        passportForRequest.setStatus(passportStatus);
        return sendCreatePassportDtoToPassportEndpointForUpdate(passport.getId(), passportForRequest);
    }

    // Common errors
    @Test
    public void updatePassport_whenPassportHasAllInvalidFields_receiveBadRequest() {
        final Passport passport = passportRepository.findAll().iterator().next();
        final CreatePassportDto invalidCreatePassportDto = getInvalidCreatePassportDto();
        final Response response = sendCreatePassportDtoToPassportEndpointForUpdate(passport.getId(),
                invalidCreatePassportDto);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePassport_whenPassportHasAllInvalidFields_receiveErrorMessages() {
        final Passport passport = passportRepository.findAll().iterator().next();
        final CreatePassportDto invalidCreatePassportDto = getInvalidCreatePassportDto();
        final Response response = sendCreatePassportDtoToPersonPassportEndpoint(passport.getId(), invalidCreatePassportDto);
        final ApiError apiErrorResponse = extractDataFromResponse(response, ApiError.class);
        AssertionsForClassTypes.assertThat(apiErrorResponse.getValidationErrors().size()).isEqualTo(5);
    }
}
