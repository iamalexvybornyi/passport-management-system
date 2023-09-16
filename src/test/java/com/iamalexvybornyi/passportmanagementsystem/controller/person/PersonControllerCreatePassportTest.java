package com.iamalexvybornyi.passportmanagementsystem.controller.person;

import com.iamalexvybornyi.passportmanagementsystem.BaseTest;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.CreatePassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportDto;
import com.iamalexvybornyi.passportmanagementsystem.model.ApiError;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.PassportType;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Status;
import io.restassured.response.Response;
import lombok.NonNull;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonControllerCreatePassportTest extends BaseTest {

    @BeforeEach
    public void createValidPerson() {
        final Person person = new Person();
        person.setId(idGeneratorUtil.generatePersonId());
        person.setName("Some Name");
        person.setBirthCountry("Country");
        person.setBirthDate(LocalDate.of(1990, 1, 1));
        personRepository.save(person);
    }

    @Test
    public void postPassport_whenPassportIsValid_receiveOk() {
        final Person person = personRepository.findAll().iterator().next();
        final Response response = sendCreatePassportDtoToPersonPassportEndpoint(person.getId(), getValidCreatePassportDto(Status.ACTIVE));
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void postPassport_whenPassportIsValid_receiveCorrectPassportDto() {
        final Person person = personRepository.findAll().iterator().next();
        final Response response = sendCreatePassportDtoToPersonPassportEndpoint(person.getId(), getValidCreatePassportDto(Status.ACTIVE));
        final PassportDto actualPassportDto = extractDataFromResponse(response, PassportDto.class);
        final PassportDto expectedPassportDto =
                passportConverter.passportToPassportDto(passportRepository.findById(actualPassportDto.getId()));
        assertThat(actualPassportDto).isEqualTo(expectedPassportDto);
    }

    // Passport Number
    @Test
    public void postPassport_whenPassportNumberHasLessCharsThanRequired_receiveBadRequest() {
        final Response response = generatePassportWithPassportNumberValueAndAttemptToCreateIt("1");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenPassportNumberHasLessCharsThanRequired_receiveRelatedMessage() {
        final Response response = generatePassportWithPassportNumberValueAndAttemptToCreateIt("1");
        extractAndVerifyApiErrorResponseForField(response, "passportNumber",
                "size must be between 5 and 20");
    }

    @Test
    public void postPassport_whenPassportNumberHasMoreCharsThanRequired_receiveBadRequest() {
        final Response response = generatePassportWithPassportNumberValueAndAttemptToCreateIt(
                IntStream.rangeClosed(1, 21).mapToObj(a -> "1").collect(Collectors.joining()));
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenPassportNumberHasMoreCharsThanRequired_receiveRelatedMessage() {
        final Response response = generatePassportWithPassportNumberValueAndAttemptToCreateIt(
                IntStream.rangeClosed(1, 21).mapToObj(a -> "1").collect(Collectors.joining()));
        extractAndVerifyApiErrorResponseForField(response, "passportNumber",
                "size must be between 5 and 20");
    }

    @Test
    public void postPassport_whenPassportNumberIsNull_receiveBadRequest() {
        final Response response = generatePassportWithPassportNumberValueAndAttemptToCreateIt(null);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenPassportNumberIsNull_receiveRelatedMessage() {
        final Response response = generatePassportWithPassportNumberValueAndAttemptToCreateIt(null);
        extractAndVerifyApiErrorResponseForField(response, "passportNumber",
                "must not be null");
    }

    @Test
    public void postPassport_whenPassportNumberIsNotUnique_receiveUnprocessableEntity() {
        final String notUniquePassportNumber = "12345";
        generatePassportWithPassportNumberValueAndAttemptToCreateIt("12345");
        final Response response = generatePassportWithPassportNumberValueAndAttemptToCreateIt(notUniquePassportNumber);
        verifyResponseStatusCode(response, HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void postPassport_whenPassportNumberIsNotUnique_receiveRelatedMessage() {
        final String notUniquePassportNumber = "12345";
        generatePassportWithPassportNumberValueAndAttemptToCreateIt("12345");
        final Response response = generatePassportWithPassportNumberValueAndAttemptToCreateIt(notUniquePassportNumber);
        extractAndVerifyApiErrorResponseForField(response, "passportNumber",
                propertyReaderUtil.getUniquePassportValidationMessage());
    }

    private Response generatePassportWithPassportNumberValueAndAttemptToCreateIt(String passportNumberValue) {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePassportDto passportForRequest = getValidCreatePassportDto(Status.ACTIVE);
        passportForRequest.setPassportNumber(passportNumberValue);
        return sendCreatePassportDtoToPersonPassportEndpoint(person.getId(), passportForRequest);
    }
    
    // Given date
    @Test
    public void postPassport_whenGivenDateHasInvalidFormat_receiveBadRequest() {
        final Response response = generatePassportWithGivenDateValueAndAttemptToCreateIt("23-12-190");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenGivenDateHasInvalidFormat_receiveRelatedMessage() {
        final Response response = generatePassportWithGivenDateValueAndAttemptToCreateIt("23-12-190");
        extractAndVerifyApiErrorResponseForField(response, "givenDate",
                propertyReaderUtil.getGivenDateValidationMessage());
    }

    @Test
    public void postPassport_whenGivenDateIsNull_receiveBadRequest() {
        final Response response = generatePassportWithGivenDateValueAndAttemptToCreateIt(null);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenGivenDateIsNull_receiveRelatedMessage() {
        final Response response = generatePassportWithGivenDateValueAndAttemptToCreateIt(null);
        extractAndVerifyApiErrorResponseForField(response, "givenDate",
                "must not be null");
    }

    private Response generatePassportWithGivenDateValueAndAttemptToCreateIt(String givenDateValue) {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePassportDto passportForRequest = getValidCreatePassportDto(Status.ACTIVE);
        passportForRequest.setGivenDate(givenDateValue);
        return sendCreatePassportDtoToPersonPassportEndpoint(person.getId(), passportForRequest);
    }

    // Department code
    @Test
    public void postPassport_whenDepartmentCodeHasInvalidFormat_receiveBadRequest() {
        final Response response = generatePassportWithDepartmentCodeValueAndAttemptToCreateIt("111-11");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenDepartmentCodeHasInvalidFormat_receiveRelatedMessage() {
        final Response response = generatePassportWithDepartmentCodeValueAndAttemptToCreateIt("111-11");
        extractAndVerifyApiErrorResponseForField(response, "departmentCode",
                propertyReaderUtil.getDepartmentCodeValidationMessage());
    }

    @Test
    public void postPassport_whenDepartmentCodeIsNull_receiveBadRequest() {
        final Response response = generatePassportWithDepartmentCodeValueAndAttemptToCreateIt(null);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenDepartmentCodeIsNull_receiveRelatedMessage() {
        final Response response = generatePassportWithDepartmentCodeValueAndAttemptToCreateIt(null);
        extractAndVerifyApiErrorResponseForField(response, "departmentCode",
                "must not be null");
    }

    private Response generatePassportWithDepartmentCodeValueAndAttemptToCreateIt(String departmentCodeValue) {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePassportDto passportForRequest = getValidCreatePassportDto(Status.ACTIVE);
        passportForRequest.setDepartmentCode(departmentCodeValue);
        return sendCreatePassportDtoToPersonPassportEndpoint(person.getId(), passportForRequest);
    }

    // Passport Type
    @Test
    public void postPassport_whenPassportTypeHasInvalidValue_receiveBadRequest() {
        final Response response = generatePassportWithPassportTypeValueAndAttemptToCreateIt(
                PassportType.INTERNAL + "1");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenPassportTypeHasInvalidValue_receiveRelatedMessage() {
        final Response response = generatePassportWithPassportTypeValueAndAttemptToCreateIt(
                PassportType.INTERNAL + "1");
        extractAndVerifyApiErrorResponseForField(response, "passportType",
                propertyReaderUtil.getPassportTypeValidationMessage());
    }

    @Test
    public void postPassport_whenPassportTypeIsNull_receiveBadRequest() {
        final Response response = generatePassportWithPassportTypeValueAndAttemptToCreateIt(null);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenPassportTypeIsNull_receiveRelatedMessage() {
        final Response response = generatePassportWithPassportTypeValueAndAttemptToCreateIt(null);
        extractAndVerifyApiErrorResponseForField(response, "passportType",
                "must not be null");
    }

    private Response generatePassportWithPassportTypeValueAndAttemptToCreateIt(String passportTypeValue) {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePassportDto passportForRequest = getValidCreatePassportDto(Status.ACTIVE);
        passportForRequest.setPassportType(passportTypeValue);
        return sendCreatePassportDtoToPersonPassportEndpoint(person.getId(), passportForRequest);
    }

    // Passport Type
    @Test
    public void postPassport_whenPassportStatusHasInvalidValue_receiveBadRequest() {
        final Response response = generatePassportWithPassportStatusValueAndAttemptToCreateIt(
                Status.ACTIVE + "1");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenPassportStatusHasInvalidValue_receiveRelatedMessage() {
        final Response response = generatePassportWithPassportStatusValueAndAttemptToCreateIt(
                Status.ACTIVE + "1");
        extractAndVerifyApiErrorResponseForField(response, "status",
                propertyReaderUtil.getPassportStatusValidationMessage());
    }

    @Test
    public void postPassport_whenPassportStatusIsNull_receiveBadRequest() {
        final Response response = generatePassportWithPassportStatusValueAndAttemptToCreateIt(null);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenPassportStatusIsNull_receiveRelatedMessage() {
        final Response response = generatePassportWithPassportStatusValueAndAttemptToCreateIt(null);
        extractAndVerifyApiErrorResponseForField(response, "status", "must not be null");
    }

    @NonNull
    private Response generatePassportWithPassportStatusValueAndAttemptToCreateIt(@Nullable String statusValue) {
        final Person person = personRepository.findAll().iterator().next();
        CreatePassportDto passportForRequest = getValidCreatePassportDto(Status.ACTIVE);
        passportForRequest.setStatus(statusValue);
        return sendCreatePassportDtoToPersonPassportEndpoint(person.getId(), passportForRequest);
    }

    // Common errors
    @Test
    public void postPassport_whenPassportHasAllInvalidFields_receiveBadRequest() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePassportDto invalidCreatePassportDto = getInvalidCreatePassportDto();
        final Response response = sendCreatePassportDtoToPersonPassportEndpoint(person.getId(), invalidCreatePassportDto);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenPassportHasAllInvalidFields_receiveErrorMessages() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePassportDto invalidCreatePassportDto = getInvalidCreatePassportDto();
        final Response response = sendCreatePassportDtoToPersonPassportEndpoint(person.getId(), invalidCreatePassportDto);
        final ApiError apiErrorResponse = extractDataFromResponse(response, ApiError.class);
        AssertionsForClassTypes.assertThat(apiErrorResponse.getValidationErrors().size()).isEqualTo(5);
    }
}
