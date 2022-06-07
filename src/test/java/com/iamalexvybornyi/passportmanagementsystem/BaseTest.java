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
import com.iamalexvybornyi.passportmanagementsystem.util.PropertyReaderUtil;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/TestValidationMessages.properties")
public class BaseTest {

    @LocalServerPort
    private int port;

    private final static String PERSON_ENDPOINT_URL = "/api/v1/persons";

    private final static String PERSON_PASSPORTS_ENDPOINT_URL = "/api/v1/persons/%d/passports";

    private final static String PASSPORT_ENDPOINT_URL = "/api/v1/passports";

    private final static String PASSPORT_DEACTIVATE_ENDPOINT_URL = "/api/v1/passports/%d/deactivate";

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

    @BeforeEach
    public void cleanUp() {
        passportRepository.deleteAll();
        personRepository.deleteAll();
    }

    protected void sendCreatePersonDtoToBasePersonEndpointAndVerifyStatusCode(CreatePersonDto createPersonDto,
                                                                            int expectedStatusCode) {
        sendCreatePersonDtoToBasePersonEndpoint(createPersonDto)
                .then()
                .statusCode(expectedStatusCode);
    }

    protected Response sendCreatePersonDtoToBasePersonEndpoint(CreatePersonDto createPersonDto) {
        return given(requestSpecification())
                .body(createPersonDto)
                .when()
                .post(PERSON_ENDPOINT_URL);
    }

    protected void sendCreatePersonDtoToBasePersonEndpointForUpdateAndVerifyStatusCode(Long id, CreatePersonDto createPersonDto,
                                                                              int expectedStatusCode) {
        sendCreatePersonDtoToBasePersonEndpointForUpdate(id, createPersonDto)
                .then()
                .statusCode(expectedStatusCode);
    }

    protected Response sendCreatePersonDtoToBasePersonEndpointForUpdate(Long id, CreatePersonDto createPersonDto) {
        return given(requestSpecification())
                .body(createPersonDto)
                .when()
                .put(PERSON_ENDPOINT_URL + "/" + id);
    }

    protected Response sendCreatePassportDtoToPersonPassportEndpoint(Long id, CreatePassportDto createPassportDto) {
        return given(requestSpecification())
                .body(createPassportDto)
                .when()
                .post(String.format(PERSON_PASSPORTS_ENDPOINT_URL, id));
    }

    protected Response getPassportsFromPassportEndpointByPersonId(Long id, String... queryParamsWithValues) {
        StringBuilder finalUrlForRequest = new StringBuilder(String.format(PERSON_PASSPORTS_ENDPOINT_URL, id));
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

    protected Response getPassportsFromPassportEndpointByPersonId(String id) {
        return given(requestSpecification())
                .when()
                .get(PERSON_PASSPORTS_ENDPOINT_URL.replace("%d", id));
    }

    protected void extractAndVerifyApiErrorResponseForField(Response response, String fieldName, String expectedMessage) {
        ApiError apiErrorResponse = response.then().extract().as(ApiError.class);
        assertThat(apiErrorResponse.getValidationErrors().get(fieldName)).isEqualTo(expectedMessage);
    }

    protected Response getPersonByIdFromPersonEndpoint(Long id) {
        return given(requestSpecification())
                .when()
                .get(PERSON_ENDPOINT_URL + "/" + id);
    }

    protected Response getPersonByIdFromPersonEndpoint(String id) {
        return given(requestSpecification())
                .when()
                .get(PERSON_ENDPOINT_URL + "/" + id);
    }

    protected Response sendCreatePassportDtoToPassportEndpointForUpdate(Long id,
                                                                              CreatePassportDto createPassportDto) {
        return given(requestSpecification())
                .body(createPassportDto)
                .when()
                .put(PASSPORT_ENDPOINT_URL + "/" + id);
    }

    protected Response sendCreatePassportDtoToPassportEndpointForUpdate(String id,
                                                                        CreatePassportDto createPassportDto) {
        return given(requestSpecification())
                .body(createPassportDto)
                .when()
                .put(PASSPORT_ENDPOINT_URL + "/" + id);
    }

    protected Response getPassportsFromPassportEndpoint(String... queryParamsWithValues) {
        StringBuilder finalUrlForRequest = new StringBuilder(PASSPORT_ENDPOINT_URL);
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

    protected Response getPassportByIdFromPassportEndpoint(Long id) {
        return given(requestSpecification())
                .when()
                .get(PASSPORT_ENDPOINT_URL + "/" + id);
    }

    protected Response getPassportByIdFromPassportEndpoint(String id) {
        return given(requestSpecification())
                .when()
                .get(PASSPORT_ENDPOINT_URL + "/" + id);
    }

    protected Response deletePassportByIdFromPassportEndpoint(Long id) {
        return given(requestSpecification())
                .when()
                .delete(PASSPORT_ENDPOINT_URL + "/" + id);
    }

    protected Response deletePassportByIdFromPassportEndpoint(String id) {
        return given(requestSpecification())
                .when()
                .delete(PASSPORT_ENDPOINT_URL + "/" + id);
    }

    protected Response deactivatePassportByIdFromPassportEndpoint(Long id) {
        return given(requestSpecification())
                .when()
                .post(String.format(PASSPORT_DEACTIVATE_ENDPOINT_URL, id));
    }

    protected Response deactivatePassportByIdFromPassportEndpoint(String id) {
        return given(requestSpecification())
                .when()
                .post(PASSPORT_DEACTIVATE_ENDPOINT_URL.replace("%d", id));
    }

    protected Response getPersonsFromPersonEndpoint(String... queryParamsWithValues) {
        StringBuilder finalUrlForRequest = new StringBuilder(PERSON_ENDPOINT_URL);
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

    protected <T> T extractDataFromResponse(Response response, Class<T> tClass) {
        return response.then().extract().as(tClass);
    }

    protected void verifyResponseStatusCode(Response response, int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    protected RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri("http://localhost:" + port)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .build();
    }

    protected CreatePersonDto getValidCreatePersonDto() {
        CreatePersonDto createPersonDto = new CreatePersonDto();
        createPersonDto.setName("Some Name");
        createPersonDto.setBirthCountry("Country");
        createPersonDto.setBirthDate("01-01-1990");
        return createPersonDto;
    }

    protected CreatePassportDto getValidCreatePassportDto() {
        CreatePassportDto createPassportDto = new CreatePassportDto();
        createPassportDto.setPassportNumber(RandomStringUtils.randomAlphanumeric(10));
        createPassportDto.setPassportType(PassportType.INTERNAL.toString());
        createPassportDto.setDepartmentCode("111-111");
        createPassportDto.setStatus(Status.ACTIVE.toString());
        createPassportDto.setGivenDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        return createPassportDto;
    }

    protected CreatePersonDto getInvalidCreatePersonDto() {
        CreatePersonDto createPersonDto = new CreatePersonDto();
        createPersonDto.setName("A");
        createPersonDto.setBirthCountry("B");
        createPersonDto.setBirthDate(null);
        return createPersonDto;
    }

    protected CreatePassportDto getInvalidCreatePassportDto() {
        CreatePassportDto createPassportDto = new CreatePassportDto();
        createPassportDto.setPassportType(null);
        createPassportDto.setPassportNumber(null);
        createPassportDto.setGivenDate(null);
        createPassportDto.setDepartmentCode(null);
        createPassportDto.setStatus(null);
        return createPassportDto;
    }

}
