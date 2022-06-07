package com.iamalexvybornyi.passportmanagementsystem.controller.person;

import com.iamalexvybornyi.passportmanagementsystem.BaseTest;
import com.iamalexvybornyi.passportmanagementsystem.dto.person.CreatePersonDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.person.PersonDto;
import com.iamalexvybornyi.passportmanagementsystem.model.ApiError;
import com.iamalexvybornyi.passportmanagementsystem.model.Person;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PersonControllerUpdatePersonTest extends BaseTest {

    @BeforeEach
    public void createValidPerson() {
        Person person = new Person();
        person.setName("Some Name");
        person.setBirthCountry("Country");
        person.setBirthDate(LocalDate.of(1990, 1, 1));
        personRepository.save(person);
    }

    @Test
    public void updatePerson_whenPersonIsValid_receiveCorrectPersonDto() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setName(createPersonDto.getName() + " updated");
        createPersonDto.setBirthCountry(createPersonDto.getBirthCountry() + " updated");
        createPersonDto.setBirthDate(LocalDate.now().minusYears(15).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        PersonDto personDto = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto)
                .then().extract().as(PersonDto.class);
        assertThat(personDto).isNotNull();
        assertThat(personDto.getBirthCountry()).isEqualTo(createPersonDto.getBirthCountry());
        assertThat(personDto.getBirthDate()).isEqualTo(createPersonDto.getBirthDate());
        assertThat(personDto.getName()).isEqualTo(createPersonDto.getName());
    }

    @Test
    public void updatePerson_whenPersonIsValid_personIsCorrectlySavedToDatasource() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setName(createPersonDto.getName() + " updated");
        createPersonDto.setBirthCountry(createPersonDto.getBirthCountry() + " updated");
        createPersonDto.setBirthDate(LocalDate.now().minusYears(15).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        PersonDto personDto = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto)
                .then().extract().as(PersonDto.class);

        Person updatedPerson = personRepository.findById(personDto.getId());
        assertThat(updatedPerson).isNotNull();
        assertThat(updatedPerson.getBirthCountry()).isEqualTo(createPersonDto.getBirthCountry());
        assertThat(updatedPerson.getBirthDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .isEqualTo(createPersonDto.getBirthDate());
        assertThat(updatedPerson.getName()).isEqualTo(createPersonDto.getName());
    }

    @Test
    public void updatePerson_whenPersonHasNameWithLessCharsThanRequired_receiveBadRequest() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setName("A");
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePerson_whenPersonHasNameWithLessCharsThanRequired_receiveRelatedMessage() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setName("A");
        Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "name", "size must be between 5 and 75");
    }

    @Test
    public void updatePerson_whenPersonHasNameWithMoreCharsThanRequired_receiveBadRequest() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setName(IntStream.rangeClosed(1, 76).mapToObj(a -> "a").collect(Collectors.joining()));
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePerson_whenPersonHasNameWithMoreCharsThanRequired_receiveRelatedMessage() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setName(IntStream.rangeClosed(1, 76).mapToObj(a -> "a").collect(Collectors.joining()));
        Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "name", "size must be between 5 and 75");
    }

    @Test
    public void updatePerson_whenPersonHasNullName_receiveRelatedMessage() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setName(null);
        Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "name", "must not be null");
    }

    @Test
    public void updatePerson_whenPersonHasNullName_receiveBadRequest() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setName(null);
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePerson_whenPersonHasBirthCountryWithLessCharsThanRequired_receiveBadRequest() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthCountry("A");
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePerson_whenPersonHasBirthCountryWithLessCharsThanRequired_receiveRelatedMessage() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthCountry("A");
        Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthCountry",
                "size must be between 2 and 50");
    }

    @Test
    public void updatePerson_whenPersonHasBirthCountryWithMoreCharsThanRequired_receiveBadRequest() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthCountry(IntStream.rangeClosed(1, 51).mapToObj(a -> "a").collect(Collectors.joining()));
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePerson_whenPersonHasBirthCountryWithMoreCharsThanRequired_receiveRelatedMessage() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthCountry(IntStream.rangeClosed(1, 51).mapToObj(a -> "a").collect(Collectors.joining()));
        Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthCountry",
                "size must be between 2 and 50");
    }

    @Test
    public void updatePerson_whenPersonHasNullBirthCountry_receiveRelatedMessage() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthCountry(null);
        Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthCountry", "must not be null");
    }

    @Test
    public void updatePerson_whenPersonHasNullBirthCountry_receiveBadRequest() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthCountry(null);
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    //
    @Test
    public void updatePerson_whenPersonHasNullBirthDate_receiveRelatedMessage() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthDate(null);
        Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthDate", "must not be null");
    }

    @Test
    public void updatePerson_whenPersonHasNullBirthDate_receiveBadRequest() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthDate(null);
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePerson_whenPersonHasInvalidBirthDateFormat_receiveRelatedMessage() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthDate("12-12-198");
        Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthDate",
                propertyReaderUtil.getBirthDateFormatValidationMessage());
    }

    @Test
    public void updatePerson_whenPersonHasInvalidBirthDateFormat_receiveBadRequest() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthDate("12-12-198");
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePerson_whenPersonHasInappropriateBirthDateValue_receiveRelatedMessage() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthDate",
                propertyReaderUtil.getBirthDateValidationMessage());
    }

    @Test
    public void updatePerson_whenPersonHasInappropriateBirthDateValue_receiveUnprocessableEntity() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void updatePerson_whenPersonHasAllInvalidFields_receiveBadRequest() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = getInvalidCreatePersonDto();
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePerson_whenPersonHasAllInvalidFields_receiveErrorMessages() {
        Person person = personRepository.findAll().iterator().next();
        CreatePersonDto createPersonDto = getInvalidCreatePersonDto();
        Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        ApiError apiErrorResponse = response.then().extract().as(ApiError.class);
        assertThat(apiErrorResponse.getValidationErrors().size()).isEqualTo(3);
    }

}
