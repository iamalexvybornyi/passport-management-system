package com.iamalexvybornyi.passportmanagementsystem.controller.passport;

import com.iamalexvybornyi.passportmanagementsystem.BaseTest;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.CreatePassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportDto;
import com.iamalexvybornyi.passportmanagementsystem.model.ApiError;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Status;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PassportControllerGetPassportsTest extends BaseTest {

    @BeforeEach
    public void createPersonAndPassports() {
        final Person person = new Person();
        person.setId(idGeneratorUtil.generatePersonId());
        person.setName("Some Name");
        person.setBirthCountry("Country");
        person.setBirthDate(LocalDate.of(1990, 1, 1));
        personRepository.save(person);

        final CreatePassportDto validPassport1 = getValidCreatePassportDto(Status.ACTIVE);
        validPassport1.setGivenDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        personService.addPersonPassport(person.getId(), validPassport1);

        final CreatePassportDto validPassport2 = getValidCreatePassportDto(Status.ACTIVE);
        validPassport2.setGivenDate(LocalDate.now().minusYears(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        personService.addPersonPassport(person.getId(), validPassport2);

        final CreatePassportDto validPassport3 = getValidCreatePassportDto(Status.ACTIVE);
        validPassport3.setGivenDate(LocalDate.now().minusYears(3).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        personService.addPersonPassport(person.getId(), validPassport3);
    }

    @Test
    public void getPassports_whenPassportsExist_receiveOk() {
        final Response response = getPassportsFromPassportEndpoint();
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassports_whenPassportsExist_receiveExpectedPassports() {
        getPassportListAndVerifyItsContents();
    }

    @Test
    public void getPassports_whenPassportsDoNotExist_receiveOk() {
        passportRepository.deleteAll();
        final Response response = getPassportsFromPassportEndpoint();
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassports_whenPassportsDoNotExist_receiveExpectedPassports() {
        passportRepository.deleteAll();
        getPassportListAndVerifyItsContents();
    }

    // Start date only
    @Test
    public void getPassports_whenPassportsExistAndStartDateIsValid_receiveOk() {
        passportRepository.deleteAll();
        final String startDateAsString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final Response response = getPassportsFromPassportEndpoint("startDate=" + startDateAsString);
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassports_whenPassportsExistAndStartDateIsValid_receiveExpectedPassports() {
        final LocalDate startDate = LocalDate.now();
        final String startDateAsString = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final List<PassportDto> expectedListOfPassports = new ArrayList<>();
        passportRepository.findAll().forEach(passport -> {
            if (passport.getGivenDate().compareTo(startDate) >= 0) {
                expectedListOfPassports.add(passportConverter.passportToPassportDto(passport));
            }
        });
        final Response response = getPassportsFromPassportEndpoint("startDate=" + startDateAsString);
        final List<PassportDto> actualListOfPassports = List.of(extractDataFromResponse(response, PassportDto[].class));
        assertThat(actualListOfPassports).isEqualTo(expectedListOfPassports);
    }

    @Test
    public void getPassports_whenPassportsExistAndStartDateHasInvalidFormat_receiveBadRequest() {
        final Response response = getPassportsFromPassportEndpoint("startDate=11-11-202");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void getPassports_whenPassportsExistAndStartDateHasInvalidFormat_receiveRelatedError() {
        final Response response = getPassportsFromPassportEndpoint("startDate=11-11-202");
        final ApiError apiError = extractDataFromResponse(response, ApiError.class);
        assertThat(apiError.getMessage()).isEqualTo("Invalid input data");
    }

    // End date only
    @Test
    public void getPassports_whenPassportsExistAndEndDateIsValid_receiveOk() {
        final String endDateAsString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final Response response = getPassportsFromPassportEndpoint("endDate=" + endDateAsString);
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassports_whenPassportsExistAndEndDateIsValid_receiveExpectedPassports() {
        final LocalDate endDate = LocalDate.now();
        final String endDateAsString = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final List<PassportDto> expectedListOfPassports = new ArrayList<>();
        passportRepository.findAll().forEach(passport -> {
            if (passport.getGivenDate().compareTo(endDate) <= 0) {
                expectedListOfPassports.add(passportConverter.passportToPassportDto(passport));
            }
        });
        final Response response = getPassportsFromPassportEndpoint("endDate=" + endDateAsString);
        final List<PassportDto> actualListOfPassports = List.of(extractDataFromResponse(response, PassportDto[].class));
        assertThat(actualListOfPassports).isEqualTo(expectedListOfPassports);
    }

    @Test
    public void getPassports_whenPassportsExistAndEndDateHasInvalidFormat_receiveBadRequest() {
        final Response response = getPassportsFromPassportEndpoint("endDate=11-11-202");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void getPassports_whenPassportsExistAndEndDateHasInvalidFormat_receiveRelatedError() {
        final Response response = getPassportsFromPassportEndpoint("endDate=11-11-202");
        final ApiError apiError = extractDataFromResponse(response, ApiError.class);
        assertThat(apiError.getMessage()).isEqualTo("Invalid input data");
    }

    // Start and End dates
    @Test
    public void getPassports_whenPassportsExistAndStartAndEndDatesAreValid_receiveOk() {
        final String startDateAsString = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final String endDateAsString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final Response response = getPassportsFromPassportEndpoint("startDate=" + startDateAsString,
                "endDate=" + endDateAsString);
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassports_whenPassportsExistAndStartAndEndDatesAreValid_receiveExpectedPassports() {
        final LocalDate startDate = LocalDate.now().minusYears(1);
        final String startDateAsString = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final LocalDate endDate = LocalDate.now();
        final String endDateAsString = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final List<PassportDto> expectedListOfPassports = new ArrayList<>();
        passportRepository.findAll().forEach(passport -> {
            if (passport.getGivenDate().compareTo(startDate) >= 0 && passport.getGivenDate().compareTo(endDate) <= 0) {
                expectedListOfPassports.add(passportConverter.passportToPassportDto(passport));
            }
        });
        final Response response = getPassportsFromPassportEndpoint("startDate=" + startDateAsString,
                "endDate=" + endDateAsString);
        final List<PassportDto> actualListOfPassports = List.of(extractDataFromResponse(response, PassportDto[].class));
        assertThat(actualListOfPassports).isEqualTo(expectedListOfPassports);
    }

    @Test
    public void getPassports_whenPassportsExistAndStartAndEndDatesAreValidAndEqual_receiveExpectedPassports() {
        final LocalDate startDate = LocalDate.now();
        final String startDateAsString = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final LocalDate endDate = LocalDate.now();
        final String endDateAsString = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final List<PassportDto> expectedListOfPassports = new ArrayList<>();
        passportRepository.findAll().forEach(passport -> {
            if (passport.getGivenDate().compareTo(startDate) >= 0 && passport.getGivenDate().compareTo(endDate) <= 0) {
                expectedListOfPassports.add(passportConverter.passportToPassportDto(passport));
            }
        });
        final Response response = getPassportsFromPassportEndpoint("startDate=" + startDateAsString,
                "endDate=" + endDateAsString);
        final List<PassportDto> actualListOfPassports = List.of(extractDataFromResponse(response, PassportDto[].class));
        assertThat(actualListOfPassports).isEqualTo(expectedListOfPassports);
    }

    @Test
    public void getPassports_whenPassportsExistAndEndDateIsGreaterThanStartDate_receiveUnprocessableEntity() {
        final String startDateAsString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final String endDateAsString = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final Response response = getPassportsFromPassportEndpoint("startDate=" + startDateAsString,
                "endDate=" + endDateAsString);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void getPassports_whenPassportsExistAndEndDateIsGreaterThanStartDate_receiveRelatedError() {
        final String startDateAsString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final String endDateAsString = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        final Response response = getPassportsFromPassportEndpoint("startDate=" + startDateAsString,
                "endDate=" + endDateAsString);
        final ApiError apiError = extractDataFromResponse(response, ApiError.class);
        assertThat(apiError.getMessage()).isEqualTo("Invalid input data");
    }

    private void getPassportListAndVerifyItsContents(String... queryParamsWithValues) {
        final List<PassportDto> expectedListOfPassports = new ArrayList<>();
        passportRepository.findAll().forEach(passport ->
                expectedListOfPassports.add(passportConverter.passportToPassportDto(passport)));
        final Response response = getPassportsFromPassportEndpoint(queryParamsWithValues);
        final List<PassportDto> actualListOfPassports = List.of(extractDataFromResponse(response, PassportDto[].class));
        assertThat(actualListOfPassports).isEqualTo(expectedListOfPassports);
    }
}
