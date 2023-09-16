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
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto,
                HttpStatus.OK.value());
    }

    @Test
    public void postPerson_whenPersonIsValid_receiveCorrectPersonDto() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        final PersonDto personDto = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto)
                .then().extract().as(PersonDto.class);
        assertThat(personDto).isNotNull();
        assertThat(personDto.getBirthCountry()).isEqualTo(createPersonDto.getBirthCountry());
        assertThat(personDto.getBirthDate()).isEqualTo(createPersonDto.getBirthDate());
        assertThat(personDto.getName()).isEqualTo(createPersonDto.getName());
    }

    @Test
    public void postPerson_whenPersonIsValid_personIsSavedToDatasource() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        final PersonDto personDto = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto)
                .then().extract().as(PersonDto.class);
        final Person personFromDataSource = personRepository.findById(personDto.getId());
        assertThat(personFromDataSource).isNotNull();
    }

    @Test
    public void postPerson_whenPersonHasNameWithLessCharsThanRequired_receiveBadRequest() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setName("A");
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPerson_whenPersonHasNameWithLessCharsThanRequired_receiveRelatedMessage() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setName("A");
        final Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "name", "size must be between 5 and 75");
    }

    @Test
    public void postPerson_whenPersonHasNameWithMoreCharsThanRequired_receiveBadRequest() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setName(IntStream.rangeClosed(1, 76).mapToObj(a -> "a").collect(Collectors.joining()));
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPerson_whenPersonHasNameWithMoreCharsThanRequired_receiveRelatedMessage() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setName(IntStream.rangeClosed(1, 76).mapToObj(a -> "a").collect(Collectors.joining()));
        final Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "name", "size must be between 5 and 75");
    }

    @Test
    public void postPerson_whenPersonHasNullName_receiveRelatedMessage() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setName(null);
        final Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "name", "must not be null");
    }

    @Test
    public void postPerson_whenPersonHasNullName_receiveBadRequest() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setName(null);
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPerson_whenPersonHasBirthCountryWithLessCharsThanRequired_receiveBadRequest() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthCountry("a");
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPerson_whenPersonHasBirthCountryWithLessCharsThanRequired_receiveRelatedMessage() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthCountry("a");
        final Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthCountry",
                "size must be between 2 and 50");
    }

    @Test
    public void postPerson_whenPersonHasBirthCountryWithMoreCharsThanRequired_receiveBadRequest() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthCountry(IntStream.rangeClosed(1, 51).mapToObj(a -> "a").collect(Collectors.joining()));
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPerson_whenPersonHasBirthCountryWithMoreCharsThanRequired_receiveRelatedMessage() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthCountry(IntStream.rangeClosed(1, 51).mapToObj(a -> "a").collect(Collectors.joining()));
        final Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthCountry",
                "size must be between 2 and 50");
    }

    @Test
    public void postPerson_whenPersonHasNullBirthCountry_receiveRelatedMessage() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthCountry(null);
        final Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthCountry", "must not be null");
    }

    @Test
    public void postPerson_whenPersonHasNullBirthCountry_receiveBadRequest() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthCountry(null);
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPerson_whenPersonHasNullBirthDate_receiveRelatedMessage() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthDate(null);
        final Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthDate", "must not be null");
    }

    @Test
    public void postPerson_whenPersonHasNullBirthDate_receiveBadRequest() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthDate(null);
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPerson_whenPersonHasInvalidBirthDateFormat_receiveRelatedMessage() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthDate("12-12-198");
        final Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthDate",
                propertyReaderUtil.getBirthDateFormatValidationMessage());
    }

    @Test
    public void postPerson_whenPersonHasInvalidBirthDateFormat_receiveBadRequest() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthDate("12-12-198");
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPerson_whenPersonHasInappropriateBirthDateValue_receiveRelatedMessage() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        final Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthDate",
                propertyReaderUtil.getBirthDateValidationMessage());
    }

    @Test
    public void postPerson_whenPersonHasInappropriateBirthDateValue_receiveUnprocessableEntity() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto,
                HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void postPerson_whenPersonIsExactly14YearsOld_receiveRelatedMessage() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthDate(LocalDate.now().minusYears(14).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthDate",
                propertyReaderUtil.getBirthDateValidationMessage());
    }

    @Test
    public void postPerson_whenPersonIsExactly14YearsOld_receiveUnprocessableEntity() {
        final CreatePersonDto createPersonDto = getValidCreatePersonDto();
        createPersonDto.setBirthDate(LocalDate.now().minusYears(14).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto,
                HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void postPerson_whenPersonHasAllInvalidFields_receiveBadRequest() {
        final CreatePersonDto createPersonDto = getInvalidCreatePersonDto();
        sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(createPersonDto, HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void postPerson_whenPersonHasAllInvalidFields_receiveErrorMessages() {
        final CreatePersonDto createPersonDto = getInvalidCreatePersonDto();
        final Response response = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto);
        final ApiError apiErrorResponse = extractDataFromResponse(response, ApiError.class);
        assertThat(apiErrorResponse.getValidationErrors().size()).isEqualTo(3);
    }
}
