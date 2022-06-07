package com.iamalexvybornyi.passportmanagementsystem.controller.person;

import com.iamalexvybornyi.passportmanagementsystem.BaseTest;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.CreatePassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportDto;
import com.iamalexvybornyi.passportmanagementsystem.model.ApiError;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.PassportType;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Status;
import io.restassured.response.Response;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonControllerCreatePassportTest extends BaseTest {

    @BeforeEach
    public void createValidPerson() {
        Person person = new Person();
        person.setName("Some Name");
        person.setBirthCountry("Country");
        person.setBirthDate(LocalDate.of(1990, 1, 1));
        personRepository.save(person);
    }

    @Test
    public void postPassport_whenPassportIsValid_receiveOk() {
        Person person = personRepository.findAll().iterator().next();
        Response response = sendCreatePassportDtoToPersonPassportEndpoint(person.getId(), getValidCreatePassportDto());
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void postPassport_whenPassportIsValid_receiveCorrectPassportDto() {
        Person person = personRepository.findAll().iterator().next();
        Response response = sendCreatePassportDtoToPersonPassportEndpoint(person.getId(), getValidCreatePassportDto());
        PassportDto actualPassportDto = extractDataFromResponse(response, PassportDto.class);
        PassportDto expectedPassportDto =
                passportConverter.passportToPassportDto(passportRepository.findById(actualPassportDto.getId()));
        assertThat(actualPassportDto).isEqualTo(expectedPassportDto);
    }

    // Passport Number
    @Test
    public void postPassport_whenPassportNumberHasLessCharsThanRequired_receiveBadRequest() {
        Response response = generatePassportWithPassportNumberValueAndAttemptToCreateIt("1");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenPassportNumberHasLessCharsThanRequired_receiveRelatedMessage() {
        Response response = generatePassportWithPassportNumberValueAndAttemptToCreateIt("1");
        extractAndVerifyApiErrorResponseForField(response, "passportNumber",
                "size must be between 5 and 20");
    }

    @Test
    public void postPassport_whenPassportNumberHasMoreCharsThanRequired_receiveBadRequest() {
        Response response = generatePassportWithPassportNumberValueAndAttemptToCreateIt(
                IntStream.rangeClosed(1, 21).mapToObj(a -> "1").collect(Collectors.joining()));
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenPassportNumberHasMoreCharsThanRequired_receiveRelatedMessage() {
        Response response = generatePassportWithPassportNumberValueAndAttemptToCreateIt(
                IntStream.rangeClosed(1, 21).mapToObj(a -> "1").collect(Collectors.joining()));
        extractAndVerifyApiErrorResponseForField(response, "passportNumber",
                "size must be between 5 and 20");
    }

    @Test
    public void postPassport_whenPassportNumberIsNull_receiveBadRequest() {
        Response response = generatePassportWithPassportNumberValueAndAttemptToCreateIt(null);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenPassportNumberIsNull_receiveRelatedMessage() {
        Response response = generatePassportWithPassportNumberValueAndAttemptToCreateIt(null);
        extractAndVerifyApiErrorResponseForField(response, "passportNumber",
                "must not be null");
    }

    @Test
    public void postPassport_whenPassportNumberIsNotUnique_receiveUnprocessableEntity() {
        String notUniquePassportNumber = "12345";
        generatePassportWithPassportNumberValueAndAttemptToCreateIt("12345");
        Response response = generatePassportWithPassportNumberValueAndAttemptToCreateIt(notUniquePassportNumber);
        verifyResponseStatusCode(response, HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void postPassport_whenPassportNumberIsNotUnique_receiveRelatedMessage() {
        String notUniquePassportNumber = "12345";
        generatePassportWithPassportNumberValueAndAttemptToCreateIt("12345");
        Response response = generatePassportWithPassportNumberValueAndAttemptToCreateIt(notUniquePassportNumber);
        extractAndVerifyApiErrorResponseForField(response, "passportNumber",
                propertyReaderUtil.getUniquePassportValidationMessage());
    }

    private Response generatePassportWithPassportNumberValueAndAttemptToCreateIt(String passportNumberValue) {
        Person person = personRepository.findAll().iterator().next();
        CreatePassportDto passportForRequest = getValidCreatePassportDto();
        passportForRequest.setPassportNumber(passportNumberValue);
        return sendCreatePassportDtoToPersonPassportEndpoint(person.getId(), passportForRequest);
    }
    
    // Given date
    @Test
    public void postPassport_whenGivenDateHasInvalidFormat_receiveBadRequest() {
        Response response = generatePassportWithGivenDateValueAndAttemptToCreateIt("23-12-190");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenGivenDateHasInvalidFormat_receiveRelatedMessage() {
        Response response = generatePassportWithGivenDateValueAndAttemptToCreateIt("23-12-190");
        extractAndVerifyApiErrorResponseForField(response, "givenDate",
                propertyReaderUtil.getGivenDateValidationMessage());
    }

    @Test
    public void postPassport_whenGivenDateIsNull_receiveBadRequest() {
        Response response = generatePassportWithGivenDateValueAndAttemptToCreateIt(null);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenGivenDateIsNull_receiveRelatedMessage() {
        Response response = generatePassportWithGivenDateValueAndAttemptToCreateIt(null);
        extractAndVerifyApiErrorResponseForField(response, "givenDate",
                "must not be null");
    }

    private Response generatePassportWithGivenDateValueAndAttemptToCreateIt(String givenDateValue) {
        Person person = personRepository.findAll().iterator().next();
        CreatePassportDto passportForRequest = getValidCreatePassportDto();
        passportForRequest.setGivenDate(givenDateValue);
        return sendCreatePassportDtoToPersonPassportEndpoint(person.getId(), passportForRequest);
    }

    // Department code
    @Test
    public void postPassport_whenDepartmentCodeHasInvalidFormat_receiveBadRequest() {
        Response response = generatePassportWithDepartmentCodeValueAndAttemptToCreateIt("111-11");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenDepartmentCodeHasInvalidFormat_receiveRelatedMessage() {
        Response response = generatePassportWithDepartmentCodeValueAndAttemptToCreateIt("111-11");
        extractAndVerifyApiErrorResponseForField(response, "departmentCode",
                propertyReaderUtil.getDepartmentCodeValidationMessage());
    }

    @Test
    public void postPassport_whenDepartmentCodeIsNull_receiveBadRequest() {
        Response response = generatePassportWithDepartmentCodeValueAndAttemptToCreateIt(null);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenDepartmentCodeIsNull_receiveRelatedMessage() {
        Response response = generatePassportWithDepartmentCodeValueAndAttemptToCreateIt(null);
        extractAndVerifyApiErrorResponseForField(response, "departmentCode",
                "must not be null");
    }

    private Response generatePassportWithDepartmentCodeValueAndAttemptToCreateIt(String departmentCodeValue) {
        Person person = personRepository.findAll().iterator().next();
        CreatePassportDto passportForRequest = getValidCreatePassportDto();
        passportForRequest.setDepartmentCode(departmentCodeValue);
        return sendCreatePassportDtoToPersonPassportEndpoint(person.getId(), passportForRequest);
    }

    // Passport Type
    @Test
    public void postPassport_whenPassportTypeHasInvalidValue_receiveBadRequest() {
        Response response = generatePassportWithPassportTypeValueAndAttemptToCreateIt(
                PassportType.INTERNAL + "1");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenPassportTypeHasInvalidValue_receiveRelatedMessage() {
        Response response = generatePassportWithPassportTypeValueAndAttemptToCreateIt(
                PassportType.INTERNAL + "1");
        extractAndVerifyApiErrorResponseForField(response, "passportType",
                propertyReaderUtil.getPassportTypeValidationMessage());
    }

    @Test
    public void postPassport_whenPassportTypeIsNull_receiveBadRequest() {
        Response response = generatePassportWithPassportTypeValueAndAttemptToCreateIt(null);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenPassportTypeIsNull_receiveRelatedMessage() {
        Response response = generatePassportWithPassportTypeValueAndAttemptToCreateIt(null);
        extractAndVerifyApiErrorResponseForField(response, "passportType",
                "must not be null");
    }

    private Response generatePassportWithPassportTypeValueAndAttemptToCreateIt(String passportTypeValue) {
        Person person = personRepository.findAll().iterator().next();
        CreatePassportDto passportForRequest = getValidCreatePassportDto();
        passportForRequest.setPassportType(passportTypeValue);
        return sendCreatePassportDtoToPersonPassportEndpoint(person.getId(), passportForRequest);
    }

    // Passport Type
    @Test
    public void postPassport_whenPassportStatusHasInvalidValue_receiveBadRequest() {
        Response response = generatePassportWithPassportStatusValueAndAttemptToCreateIt(
                Status.ACTIVE + "1");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenPassportStatusHasInvalidValue_receiveRelatedMessage() {
        Response response = generatePassportWithPassportStatusValueAndAttemptToCreateIt(
                Status.ACTIVE + "1");
        extractAndVerifyApiErrorResponseForField(response, "status",
                propertyReaderUtil.getPassportStatusValidationMessage());
    }

    @Test
    public void postPassport_whenPassportStatusIsNull_receiveBadRequest() {
        Response response = generatePassportWithPassportStatusValueAndAttemptToCreateIt(null);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenPassportStatusIsNull_receiveRelatedMessage() {
        Response response = generatePassportWithPassportStatusValueAndAttemptToCreateIt(null);
        extractAndVerifyApiErrorResponseForField(response, "status", "must not be null");
    }

    private Response generatePassportWithPassportStatusValueAndAttemptToCreateIt(String statusValue) {
        Person person = personRepository.findAll().iterator().next();
        CreatePassportDto passportForRequest = getValidCreatePassportDto();
        passportForRequest.setStatus(statusValue);
        return sendCreatePassportDtoToPersonPassportEndpoint(person.getId(), passportForRequest);
    }

    // Common errors
    @Test
    public void postPassport_whenPassportHasAllInvalidFields_receiveBadRequest() {
        Person person = personRepository.findAll().iterator().next();
        CreatePassportDto invalidCreatePassportDto = getInvalidCreatePassportDto();
        Response response = sendCreatePassportDtoToPersonPassportEndpoint(person.getId(), invalidCreatePassportDto);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPassport_whenPassportHasAllInvalidFields_receiveErrorMessages() {
        Person person = personRepository.findAll().iterator().next();
        CreatePassportDto invalidCreatePassportDto = getInvalidCreatePassportDto();
        Response response = sendCreatePassportDtoToPersonPassportEndpoint(person.getId(), invalidCreatePassportDto);
        ApiError apiErrorResponse = extractDataFromResponse(response, ApiError.class);
        AssertionsForClassTypes.assertThat(apiErrorResponse.getValidationErrors().size()).isEqualTo(5);
    }
}
