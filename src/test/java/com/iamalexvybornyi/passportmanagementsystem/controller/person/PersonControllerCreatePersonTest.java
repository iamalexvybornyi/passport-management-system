package com.iamalexvybornyi.passportmanagementsystem.controller.person;

import com.iamalexvybornyi.passportmanagementsystem.BaseTest;
import com.iamalexvybornyi.passportmanagementsystem.dto.person.CreatePersonDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.person.PersonDto;
import com.iamalexvybornyi.passportmanagementsystem.model.ApiError;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PersonControllerCreatePersonTest extends BaseTest {

    @Test
    public void postPerson_whenPersonIsValid_receiveOk() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto,
                HttpStatus.OK.value());
    }

    @Test
    public void postPerson_whenPersonIsValid_receiveCorrectPersonDto() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        PersonDto personDto = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto)
                .then().extract().as(PersonDto.class);
        assertThat(personDto).isNotNull();
        assertThat(personDto.getBirthCountry()).isEqualTo(createPersonDto.getBirthCountry());
        assertThat(personDto.getBirthDate()).isEqualTo(createPersonDto.getBirthDate());
        assertThat(personDto.getName()).isEqualTo(createPersonDto.getName());
    }

    @Test
    public void postPerson_whenPersonIsValid_personIsSavedToDatasource() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        PersonDto personDto = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto)
                .then().extract().as(PersonDto.class);
        Person personFromDataSource = personRepository.findById(personDto.getId());
        assertThat(personFromDataSource).isNotNull();
    }

    @Test
    public void postPerson_whenPersonHasNameWithLessCharsThanRequired_receiveBadRequest() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setName("A");
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPerson_whenPersonHasNameWithLessCharsThanRequired_receiveRelatedMessage() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setName("A");
        Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "name", "size must be between 5 and 75");
    }

    @Test
    public void postPerson_whenPersonHasNameWithMoreCharsThanRequired_receiveBadRequest() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setName(IntStream.rangeClosed(1, 76).mapToObj(a -> "a").collect(Collectors.joining()));
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPerson_whenPersonHasNameWithMoreCharsThanRequired_receiveRelatedMessage() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setName(IntStream.rangeClosed(1, 76).mapToObj(a -> "a").collect(Collectors.joining()));
        Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "name", "size must be between 5 and 75");
    }

    @Test
    public void postPerson_whenPersonHasNullName_receiveRelatedMessage() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setName(null);
        Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "name", "must not be null");
    }

    @Test
    public void postPerson_whenPersonHasNullName_receiveBadRequest() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setName(null);
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPerson_whenPersonHasBirthCountryWithLessCharsThanRequired_receiveBadRequest() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthCountry("a");
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPerson_whenPersonHasBirthCountryWithLessCharsThanRequired_receiveRelatedMessage() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthCountry("a");
        Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthCountry",
                "size must be between 2 and 50");
    }

    @Test
    public void postPerson_whenPersonHasBirthCountryWithMoreCharsThanRequired_receiveBadRequest() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthCountry(IntStream.rangeClosed(1, 51).mapToObj(a -> "a").collect(Collectors.joining()));
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPerson_whenPersonHasBirthCountryWithMoreCharsThanRequired_receiveRelatedMessage() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthCountry(IntStream.rangeClosed(1, 51).mapToObj(a -> "a").collect(Collectors.joining()));
        Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthCountry",
                "size must be between 2 and 50");
    }

    @Test
    public void postPerson_whenPersonHasNullBirthCountry_receiveRelatedMessage() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthCountry(null);
        Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthCountry", "must not be null");
    }

    @Test
    public void postPerson_whenPersonHasNullBirthCountry_receiveBadRequest() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthCountry(null);
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPerson_whenPersonHasNullBirthDate_receiveRelatedMessage() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthDate(null);
        Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthDate", "must not be null");
    }

    @Test
    public void postPerson_whenPersonHasNullBirthDate_receiveBadRequest() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthDate(null);
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPerson_whenPersonHasInvalidBirthDateFormat_receiveRelatedMessage() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthDate("12-12-198");
        Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthDate",
                propertyReaderUtil.getBirthDateFormatValidationMessage());
    }

    @Test
    public void postPerson_whenPersonHasInvalidBirthDateFormat_receiveBadRequest() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthDate("12-12-198");
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPerson_whenPersonHasInappropriateBirthDateValue_receiveRelatedMessage() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthDate",
                propertyReaderUtil.getBirthDateValidationMessage());
    }

    @Test
    public void postPerson_whenPersonHasInappropriateBirthDateValue_receiveUnprocessableEntity() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto,
                HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void postPerson_whenPersonIsExactly14YearsOld_receiveRelatedMessage() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthDate(LocalDate.now().minusYears(14).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthDate",
                propertyReaderUtil.getBirthDateValidationMessage());
    }

    @Test
    public void postPerson_whenPersonIsExactly14YearsOld_receiveUnprocessableEntity() {
        CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthDate(LocalDate.now().minusYears(14).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto,
                HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void postPerson_whenPersonHasAllInvalidFields_receiveBadRequest() {
        CreatePersonDto createPersonDto = getInvalidCreatePersonDto();
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPerson_whenPersonHasAllInvalidFields_receiveErrorMessages() {
        CreatePersonDto createPersonDto = getInvalidCreatePersonDto();
        Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        ApiError apiErrorResponse = extractDataFromResponse(response, ApiError.class);
        assertThat(apiErrorResponse.getValidationErrors().size()).isEqualTo(3);
    }
}
