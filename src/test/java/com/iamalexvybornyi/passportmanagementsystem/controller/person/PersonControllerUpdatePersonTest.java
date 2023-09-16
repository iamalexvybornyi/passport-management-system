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
        final Person person = new Person();
        person.setId(idGeneratorUtil.generatePersonId());
        person.setName("Some Name");
        person.setBirthCountry("Country");
        person.setBirthDate(LocalDate.of(1990, 1, 1));
        personRepository.save(person);
    }

    @Test
    public void updatePerson_whenPersonIsValid_receiveCorrectPersonDto() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
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
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setName(createPersonDto.getName() + " updated");
        createPersonDto.setBirthCountry(createPersonDto.getBirthCountry() + " updated");
        createPersonDto.setBirthDate(LocalDate.now().minusYears(15).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        final PersonDto personDto = sendCreatePersonDtoToBasePersonEndpoint(createPersonDto)
                .then().extract().as(PersonDto.class);

        final Person updatedPerson = personRepository.findById(personDto.getId());
        assertThat(updatedPerson).isNotNull();
        assertThat(updatedPerson.getBirthCountry()).isEqualTo(createPersonDto.getBirthCountry());
        assertThat(updatedPerson.getBirthDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .isEqualTo(createPersonDto.getBirthDate());
        assertThat(updatedPerson.getName()).isEqualTo(createPersonDto.getName());
    }

    @Test
    public void updatePerson_whenPersonHasNameWithLessCharsThanRequired_receiveBadRequest() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setName("A");
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePerson_whenPersonHasNameWithLessCharsThanRequired_receiveRelatedMessage() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setName("A");
        final Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "name", "size must be between 5 and 75");
    }

    @Test
    public void updatePerson_whenPersonHasNameWithMoreCharsThanRequired_receiveBadRequest() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setName(IntStream.rangeClosed(1, 76).mapToObj(a -> "a").collect(Collectors.joining()));
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePerson_whenPersonHasNameWithMoreCharsThanRequired_receiveRelatedMessage() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setName(IntStream.rangeClosed(1, 76).mapToObj(a -> "a").collect(Collectors.joining()));
        final Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "name", "size must be between 5 and 75");
    }

    @Test
    public void updatePerson_whenPersonHasNullName_receiveRelatedMessage() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setName(null);
        final Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "name", "must not be null");
    }

    @Test
    public void updatePerson_whenPersonHasNullName_receiveBadRequest() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setName(null);
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePerson_whenPersonHasBirthCountryWithLessCharsThanRequired_receiveBadRequest() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthCountry("A");
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePerson_whenPersonHasBirthCountryWithLessCharsThanRequired_receiveRelatedMessage() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthCountry("A");
        final Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthCountry",
                "size must be between 2 and 50");
    }

    @Test
    public void updatePerson_whenPersonHasBirthCountryWithMoreCharsThanRequired_receiveBadRequest() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthCountry(IntStream.rangeClosed(1, 51).mapToObj(a -> "a").collect(Collectors.joining()));
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePerson_whenPersonHasBirthCountryWithMoreCharsThanRequired_receiveRelatedMessage() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthCountry(IntStream.rangeClosed(1, 51).mapToObj(a -> "a").collect(Collectors.joining()));
        final Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthCountry",
                "size must be between 2 and 50");
    }

    @Test
    public void updatePerson_whenPersonHasNullBirthCountry_receiveRelatedMessage() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthCountry(null);
        final Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthCountry", "must not be null");
    }

    @Test
    public void updatePerson_whenPersonHasNullBirthCountry_receiveBadRequest() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthCountry(null);
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    //
    @Test
    public void updatePerson_whenPersonHasNullBirthDate_receiveRelatedMessage() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthDate(null);
        final Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthDate", "must not be null");
    }

    @Test
    public void updatePerson_whenPersonHasNullBirthDate_receiveBadRequest() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthDate(null);
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePerson_whenPersonHasInvalidBirthDateFormat_receiveRelatedMessage() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthDate("12-12-198");
        final Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthDate",
                propertyReaderUtil.getBirthDateFormatValidationMessage());
    }

    @Test
    public void updatePerson_whenPersonHasInvalidBirthDateFormat_receiveBadRequest() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthDate("12-12-198");
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePerson_whenPersonHasInappropriateBirthDateValue_receiveRelatedMessage() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        final Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        extractAndVerifyApiErrorResponseForField(response, "birthDate",
                propertyReaderUtil.getBirthDateValidationMessage());
    }

    @Test
    public void updatePerson_whenPersonHasInappropriateBirthDateValue_receiveUnprocessableEntity() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = personConverter.personToCreatePersonDto(person);
        createPersonDto.setBirthDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void updatePerson_whenPersonHasAllInvalidFields_receiveBadRequest() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = getInvalidCreatePersonDto();
        sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(person.getId(), createPersonDto,
                HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void updatePerson_whenPersonHasAllInvalidFields_receiveErrorMessages() {
        final Person person = personRepository.findAll().iterator().next();
        final CreatePersonDto createPersonDto = getInvalidCreatePersonDto();
        final Response response = sendCreatePersonDtoToBasePersonEndpointForUpdate(person.getId(), createPersonDto);
        final ApiError apiErrorResponse = response.then().extract().as(ApiError.class);
        assertThat(apiErrorResponse.getValidationErrors().size()).isEqualTo(3);
    }

}
