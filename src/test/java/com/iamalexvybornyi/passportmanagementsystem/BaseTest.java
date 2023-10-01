package com.iamalexvybornyi.passportmanagementsystem;

import com.iamalexvybornyi.passportmanagementsystem.converter.PassportConverter;
import com.iamalexvybornyi.passportmanagementsystem.converter.PersonConverter;
import com.iamalexvybornyi.passportmanagementsystem.dto.passport.CreatePassportDto;
import com.iamalexvybornyi.passportmanagementsystem.dto.person.CreatePersonDto;
import com.iamalexvybornyi.passportmanagementsystem.model.ApiError;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.PassportType;
import com.iamalexvybornyi.passportmanagementsystem.model.passport.Status;
import com.iamalexvybornyi.passportmanagementsystem.repository.PassportRepository;
import com.iamalexvybornyi.passportmanagementsystem.repository.PersonRepository;
import com.iamalexvybornyi.passportmanagementsystem.service.PassportService;
import com.iamalexvybornyi.passportmanagementsystem.service.PersonService;
import com.iamalexvybornyi.passportmanagementsystem.util.IdGeneratorUtil;
import com.iamalexvybornyi.passportmanagementsystem.util.PropertyReaderUtil;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.NonNull;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"/TestValidationMessages.properties"})
public class BaseTest {

    @LocalServerPort
    private int port;

    private final static String PERSON_ENDPOINT_URL = "/api/v1/persons";

    private final static String PERSON_PASSPORTS_ENDPOINT_URL = "/api/v1/persons/%s/passports";

    private final static String PASSPORT_ENDPOINT_URL = "/api/v1/passports";

    private final static String PASSPORT_DEACTIVATE_ENDPOINT_URL = "/api/v1/passports/%s/deactivate";

    protected static final String NON_EXISTING_ID = "non_existing_id";

    @Autowired
    protected PersonRepository personRepository;

    @Autowired
    protected PassportRepository passportRepository;

    @Autowired
    protected PersonConverter personConverter;

    @Autowired
    protected PassportConverter passportConverter;

    @Autowired
    protected PersonService personService;

    @Autowired
    protected PassportService passportService;

    @Autowired
    protected PropertyReaderUtil propertyReaderUtil;

    @Autowired
    protected IdGeneratorUtil idGeneratorUtil;

    @BeforeEach
    public void cleanUp() {
        passportRepository.deleteAll();
        personRepository.deleteAll();
    }

    protected void sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(@NonNull CreatePersonDto createPersonDto,
                                                                              int expectedStatusCode) {
        sendCreatePersonDtoToBasePersonEndpoint(createPersonDto)
                .then()
                .statusCode(expectedStatusCode);
    }

    @NonNull
    protected Response sendCreatePersonDtoToBasePersonEndpoint(@NonNull CreatePersonDto createPersonDto) {
        return given(requestSpecification())
                .body(createPersonDto)
                .when()
                .post(PERSON_ENDPOINT_URL);
    }

    protected void sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(@NonNull String id,
                                                                                       @NonNull CreatePersonDto createPersonDto,
                                                                                       int expectedStatusCode) {
        sendCreatePersonDtoToBasePersonEndpointForUpdate(id, createPersonDto)
                .then()
                .statusCode(expectedStatusCode);
    }

    @NonNull
    protected Response sendCreatePersonDtoToBasePersonEndpointForUpdate(@NonNull String id,
                                                                        @NonNull CreatePersonDto createPersonDto) {
        return given(requestSpecification())
                .body(createPersonDto)
                .when()
                .put(PERSON_ENDPOINT_URL + "/" + id);
    }

    @NonNull
    protected Response sendCreatePassportDtoToPersonPassportEndpoint(@NonNull String id,
                                                                     @NonNull CreatePassportDto createPassportDto) {
        return given(requestSpecification())
                .body(createPassportDto)
                .when()
                .post(String.format(PERSON_PASSPORTS_ENDPOINT_URL, id));
    }

    @NonNull
    protected Response getPassportsFromPassportEndpointByPersonId(@NonNull String id,
                                                                  @NonNull String... queryParamsWithValues) {
        final StringBuilder finalUrlForRequest = new StringBuilder(String.format(PERSON_PASSPORTS_ENDPOINT_URL, id));
        if (queryParamsWithValues.length > 0) {
            finalUrlForRequest.append("?");
            for (String s : queryParamsWithValues) {
                finalUrlForRequest.append(s).append("&");
            }
        }
        return given(requestSpecification())
                .when()
                .get(finalUrlForRequest.toString());
    }

    @NonNull
    protected Response getPassportsFromPassportEndpointByPersonId(@NonNull String id) {
        return given(requestSpecification())
                .when()
                .get(String.format(PERSON_PASSPORTS_ENDPOINT_URL, id));
    }

    protected void extractAndVerifyApiErrorResponseForField(@NonNull Response response,
                                                            @NonNull String fieldName,
                                                            @NonNull String expectedMessage) {
        final ApiError apiErrorResponse = response.then().extract().as(ApiError.class);
        assertThat(apiErrorResponse.getValidationErrors().get(fieldName)).isEqualTo(expectedMessage);
    }

    @NonNull
    protected Response getPersonByIdFromPersonEndpoint(@NonNull String id) {
        return given(requestSpecification())
                .when()
                .get(PERSON_ENDPOINT_URL + "/" + id);
    }

    @NonNull
    protected Response sendCreatePassportDtoToPassportEndpointForUpdate(@NonNull String id,
                                                                        @NonNull CreatePassportDto createPassportDto) {
        return given(requestSpecification())
                .body(createPassportDto)
                .when()
                .put(PASSPORT_ENDPOINT_URL + "/" + id);
    }

    @NonNull
    protected Response getPassportsFromPassportEndpoint(@NonNull String... queryParamsWithValues) {
        final StringBuilder finalUrlForRequest = new StringBuilder(PASSPORT_ENDPOINT_URL);
        if (queryParamsWithValues.length > 0) {
            finalUrlForRequest.append("?");
            for (String s : queryParamsWithValues) {
                finalUrlForRequest.append(s).append("&");
            }
        }
        return given(requestSpecification())
                .when()
                .get(finalUrlForRequest.toString());
    }

    @NonNull
    protected Response getPassportByIdFromPassportEndpoint(@NonNull String id) {
        return given(requestSpecification())
                .when()
                .get(PASSPORT_ENDPOINT_URL + "/" + id);
    }

    @NonNull
    protected Response deletePassportByIdFromPassportEndpoint(@NonNull String id) {
        return given(requestSpecification())
                .when()
                .delete(PASSPORT_ENDPOINT_URL + "/" + id);
    }

    @NonNull
    protected Response deactivatePassportByIdFromPassportEndpoint(@NonNull String id) {
        return given(requestSpecification())
                .when()
                .post(String.format(PASSPORT_DEACTIVATE_ENDPOINT_URL, id));
    }

    @NonNull
    protected Response getPersonsFromPersonEndpoint(@NonNull String... queryParamsWithValues) {
        final StringBuilder finalUrlForRequest = new StringBuilder(PERSON_ENDPOINT_URL);
        if (queryParamsWithValues.length > 0) {
            finalUrlForRequest.append("?");
            for (String s : queryParamsWithValues) {
                finalUrlForRequest.append(s).append("&");
            }
        }
        return given(requestSpecification())
                .when()
                .get(finalUrlForRequest.toString());
    }

    @NonNull
    protected <T> T extractDataFromResponse(@NonNull Response response, @NonNull Class<T> tClass) {
        return response.then().extract().as(tClass);
    }

    protected void verifyResponseStatusCode(@NonNull Response response, int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    @NonNull
    protected RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri("http://localhost:" + port)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .build();
    }

    @NonNull
    protected CreatePersonDto getValidCreatePersonDto() {
        final CreatePersonDto createPersonDto = new CreatePersonDto();
        createPersonDto.setName("Some Name");
        createPersonDto.setBirthCountry("Country");
        createPersonDto.setBirthDate("1990-01-01");
        return createPersonDto;
    }

    @NonNull
    protected CreatePassportDto getValidCreatePassportDto(@NonNull Status status) {
        final CreatePassportDto createPassportDto = new CreatePassportDto();
        createPassportDto.setPassportNumber(RandomStringUtils.randomAlphanumeric(10));
        createPassportDto.setPassportType(PassportType.INTERNAL.toString());
        createPassportDto.setDepartmentCode("111-111");
        createPassportDto.setStatus(status.toString());
        createPassportDto.setGivenDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return createPassportDto;
    }

    @NonNull
    protected CreatePersonDto getInvalidCreatePersonDto() {
        final CreatePersonDto createPersonDto = new CreatePersonDto();
        createPersonDto.setName("A");
        createPersonDto.setBirthCountry("B");
        createPersonDto.setBirthDate(null);
        return createPersonDto;
    }

    @NonNull
    protected CreatePassportDto getInvalidCreatePassportDto() {
        final CreatePassportDto createPassportDto = new CreatePassportDto();
        createPassportDto.setPassportType(null);
        createPassportDto.setPassportNumber(null);
        createPassportDto.setGivenDate(null);
        createPassportDto.setDepartmentCode(null);
        createPassportDto.setStatus(null);
        return createPassportDto;
    }

}
