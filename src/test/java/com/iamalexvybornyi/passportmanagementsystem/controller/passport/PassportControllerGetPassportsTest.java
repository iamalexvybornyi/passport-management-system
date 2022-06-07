package com.iamalexvybornyi.passportmanagementsystem.controller.passport;

import com.iamalexvybornyi.passportmanagementsystem.BaseTest;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.CreatePassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.PassportDto;
import com.iamalexvybornyi.passportmanagementsystem.model.ApiError;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
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
        Person person = new Person();
        person.setName("Some Name");
        person.setBirthCountry("Country");
        person.setBirthDate(LocalDate.of(1990, 1, 1));
        personRepository.save(person);

        CreatePassportDto validPassport1 = getValidCreatePassportDto();
        validPassport1.setGivenDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        personService.addPersonPassport(person.getId(), validPassport1);

        CreatePassportDto validPassport2 = getValidCreatePassportDto();
        validPassport2.setGivenDate(LocalDate.now().minusYears(2).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        personService.addPersonPassport(person.getId(), validPassport2);

        CreatePassportDto validPassport3 = getValidCreatePassportDto();
        validPassport3.setGivenDate(LocalDate.now().minusYears(3).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        personService.addPersonPassport(person.getId(), validPassport3);
    }

    @Test
    public void getPassports_whenPassportsExist_receiveOk() {
        Response response = getPassportsFromPassportEndpoint();
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassports_whenPassportsExist_receiveExpectedPassports() {
        getPassportListAndVerifyItsContents();
    }

    @Test
    public void getPassports_whenPassportsDoNotExist_receiveOk() {
        passportRepository.deleteAll();
        Response response = getPassportsFromPassportEndpoint();
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
        String startDateAsString = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Response response = getPassportsFromPassportEndpoint("startDate=" + startDateAsString);
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassports_whenPassportsExistAndStartDateIsValid_receiveExpectedPassports() {
        LocalDate startDate = LocalDate.now();
        String startDateAsString = startDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        List<PassportDto> expectedListOfPassports = new ArrayList<>();
        passportRepository.findAll().forEach(passport -> {
            if (passport.getGivenDate().compareTo(startDate) >= 0) {
                expectedListOfPassports.add(passportConverter.passportToPassportDto(passport));
            }
        });
        Response response = getPassportsFromPassportEndpoint("startDate=" + startDateAsString);
        List<PassportDto> actualListOfPassports = List.of(extractDataFromResponse(response, PassportDto[].class));
        assertThat(actualListOfPassports).isEqualTo(expectedListOfPassports);
    }

    @Test
    public void getPassports_whenPassportsExistAndStartDateHasInvalidFormat_receiveBadRequest() {
        Response response = getPassportsFromPassportEndpoint("startDate=11-11-202");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void getPassports_whenPassportsExistAndStartDateHasInvalidFormat_receiveRelatedError() {
        Response response = getPassportsFromPassportEndpoint("startDate=11-11-202");
        ApiError apiError = extractDataFromResponse(response, ApiError.class);
        assertThat(apiError.getMessage()).isEqualTo("Invalid input data");
    }

    // End date only
    @Test
    public void getPassports_whenPassportsExistAndEndDateIsValid_receiveOk() {
        String endDateAsString = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Response response = getPassportsFromPassportEndpoint("endDate=" + endDateAsString);
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassports_whenPassportsExistAndEndDateIsValid_receiveExpectedPassports() {
        LocalDate endDate = LocalDate.now();
        String endDateAsString = endDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        List<PassportDto> expectedListOfPassports = new ArrayList<>();
        passportRepository.findAll().forEach(passport -> {
            if (passport.getGivenDate().compareTo(endDate) <= 0) {
                expectedListOfPassports.add(passportConverter.passportToPassportDto(passport));
            }
        });
        Response response = getPassportsFromPassportEndpoint("endDate=" + endDateAsString);
        List<PassportDto> actualListOfPassports = List.of(extractDataFromResponse(response, PassportDto[].class));
        assertThat(actualListOfPassports).isEqualTo(expectedListOfPassports);
    }

    @Test
    public void getPassports_whenPassportsExistAndEndDateHasInvalidFormat_receiveBadRequest() {
        Response response = getPassportsFromPassportEndpoint("endDate=11-11-202");
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void getPassports_whenPassportsExistAndEndDateHasInvalidFormat_receiveRelatedError() {
        Response response = getPassportsFromPassportEndpoint("endDate=11-11-202");
        ApiError apiError = extractDataFromResponse(response, ApiError.class);
        assertThat(apiError.getMessage()).isEqualTo("Invalid input data");
    }

    // Start and End dates
    @Test
    public void getPassports_whenPassportsExistAndStartAndEndDatesAreValid_receiveOk() {
        String startDateAsString = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String endDateAsString = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Response response = getPassportsFromPassportEndpoint("startDate=" + startDateAsString,
                "endDate=" + endDateAsString);
        verifyResponseStatusCode(response, HttpStatus.OK.value());
    }

    @Test
    public void getPassports_whenPassportsExistAndStartAndEndDatesAreValid_receiveExpectedPassports() {
        LocalDate startDate = LocalDate.now().minusYears(1);
        String startDateAsString = startDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate endDate = LocalDate.now();
        String endDateAsString = endDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        List<PassportDto> expectedListOfPassports = new ArrayList<>();
        passportRepository.findAll().forEach(passport -> {
            if (passport.getGivenDate().compareTo(startDate) >= 0 && passport.getGivenDate().compareTo(endDate) <= 0) {
                expectedListOfPassports.add(passportConverter.passportToPassportDto(passport));
            }
        });
        Response response = getPassportsFromPassportEndpoint("startDate=" + startDateAsString,
                "endDate=" + endDateAsString);
        List<PassportDto> actualListOfPassports = List.of(extractDataFromResponse(response, PassportDto[].class));
        assertThat(actualListOfPassports).isEqualTo(expectedListOfPassports);
    }

    @Test
    public void getPassports_whenPassportsExistAndStartAndEndDatesAreValidAndEqual_receiveExpectedPassports() {
        LocalDate startDate = LocalDate.now();
        String startDateAsString = startDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate endDate = LocalDate.now();
        String endDateAsString = endDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        List<PassportDto> expectedListOfPassports = new ArrayList<>();
        passportRepository.findAll().forEach(passport -> {
            if (passport.getGivenDate().compareTo(startDate) >= 0 && passport.getGivenDate().compareTo(endDate) <= 0) {
                expectedListOfPassports.add(passportConverter.passportToPassportDto(passport));
            }
        });
        Response response = getPassportsFromPassportEndpoint("startDate=" + startDateAsString,
                "endDate=" + endDateAsString);
        List<PassportDto> actualListOfPassports = List.of(extractDataFromResponse(response, PassportDto[].class));
        assertThat(actualListOfPassports).isEqualTo(expectedListOfPassports);
    }

    @Test
    public void getPassports_whenPassportsExistAndEndDateIsGreaterThanStartDate_receiveUnprocessableEntity() {
        String startDateAsString = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String endDateAsString = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Response response = getPassportsFromPassportEndpoint("startDate=" + startDateAsString,
                "endDate=" + endDateAsString);
        verifyResponseStatusCode(response, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void getPassports_whenPassportsExistAndEndDateIsGreaterThanStartDate_receiveRelatedError() {
        String startDateAsString = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String endDateAsString = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Response response = getPassportsFromPassportEndpoint("startDate=" + startDateAsString,
                "endDate=" + endDateAsString);
        ApiError apiError = extractDataFromResponse(response, ApiError.class);
        assertThat(apiError.getMessage()).isEqualTo("Invalid input data");
    }

    private void getPassportListAndVerifyItsContents(String... queryParamsWithValues) {
        List<PassportDto> expectedListOfPassports = new ArrayList<>();
        passportRepository.findAll().forEach(passport ->
                expectedListOfPassports.add(passportConverter.passportToPassportDto(passport)));
        Response response = getPassportsFromPassportEndpoint(queryParamsWithValues);
        List<PassportDto> actualListOfPassports = List.of(extractDataFromResponse(response, PassportDto[].class));
        assertThat(actualListOfPassports).isEqualTo(expectedListOfPassports);
    }
}
