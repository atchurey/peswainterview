package com.example.demo.controllers;

import static com.example.demo.utils.CommonFieldDescriptors.*;

import com.example.demo.controllers.v1.EmployeesController;
import com.example.demo.dtos.*;
import com.example.demo.enums.Role;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.LeaveRepository;
import com.example.demo.repositories.LeaveRequestRepository;
import com.example.demo.services.impl.EmployeeServiceImpl;
import com.github.javafaker.Faker;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.demo.utils.EmployeesControllerFieldDescriptors.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeesController.class)
public class EmployeesControllerIntegrationTests {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @MockBean
    private LeaveRequestRepository leaveRequestRepository;
    @MockBean
    private LeaveRepository leaveRepository;
    @MockBean
    private EmployeeRepository employeeRepository;
    @MockBean
    private EmployeeServiceImpl employeeService;
    @MockBean
    private ModelMapper modelMapper;
    private RequestSpecification requestSpecification;

    private Gson gson;

    private Faker faker;

    private EmployeeDto supervisor = null;
    private EmployeeDto employee = null;

    public void setupRequestSpecBuilder() {
        this.requestSpecification = new RequestSpecBuilder()
                .addFilter(
                        documentationConfiguration(this.restDocumentation)
                                .operationPreprocessors()
                                .withRequestDefaults(
                                        modifyUris()
                                                .port(8080).
                                                host("localhost")
                                                .scheme("http"), prettyPrint()
                                )
                                .withResponseDefaults(prettyPrint())
                ).setConfig(RestAssuredConfig.config().objectMapperConfig(new ObjectMapperConfig().gsonObjectMapperFactory(
                        (type, s) -> gson
                )))
                .build();
    }

    private void provideGson() {
        gson = new GsonBuilder()
                .disableHtmlEscaping()
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Before
    public void setup() {
        provideGson();
        setupRequestSpecBuilder();
        getEmployeeForTest();
        fetchRandomSupervisorForTest();
        faker = new Faker();
    }

    @Test
    public void givenThereAreEmployees_whenGetAllEmployees_thenReturnStatusOkAndPagedEmployees() throws Exception {

        ArrayList<FieldDescriptor> a = new ArrayList<>(Arrays.asList(apiResponseFd));
        a.addAll(Arrays.asList(pagedContentFd));
        a.addAll(Arrays.asList(employeeListFd));

        FieldDescriptor[] fieldDescriptors = new FieldDescriptor[a.size()];
        fieldDescriptors = a.toArray(fieldDescriptors);

        RestAssured.given()
                .spec(requestSpecification)
                .contentType("application/json")
                .accept("application/json")
                .filter(document("employees-controller/get-all-employees",
                        queryParameters(
                                parameterWithName("page").description("The page to retrieve"),
                                parameterWithName("size").description("Entries per page")
                        ),
                        responseFields(
                        fieldDescriptors)))
                .when()
                .port(8080)
                .log().all()
                .when()
                .get("/employees?page=0&size=1")
                .then()
                .log().all()
                .log().ifError()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void givenAnEmployeeExists_whenGetEmployeeById_thenReturnStatusOkAndTheEmployee() throws Exception {

        ArrayList<FieldDescriptor> a = new ArrayList<>(Arrays.asList(apiResponseFd));
        a.addAll(Arrays.asList(employeeFd));

        FieldDescriptor[] fieldDescriptors = new FieldDescriptor[a.size()];
        fieldDescriptors = a.toArray(fieldDescriptors);

        RestAssured.given()
                .spec(requestSpecification)
                .contentType("application/json")
                .accept("application/json")
                .filter(document("employees-controller/get-employee-by-id", responseFields(
                        fieldDescriptors)))
                .when()
                .port(8080)
                .log().all()
                .when()
                .get("/employees/" + employee.getId())
                .then()
                .log().all()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void givenAnEmployee_whenCreateEmployee_thenReturnStatusOkAndCreatedEmployee() {

        CreateEmployeeDto payload = new CreateEmployeeDto();
        payload.setFirstName(faker.name().firstName());
        payload.setLastName(faker.name().lastName());
        payload.setEmail(faker.internet().emailAddress());
        payload.setPhone("233" + faker.random().nextInt(244000000, 259000000));
        payload.setCountryCode("GH");
        payload.setRole(Role.values()[faker.random().nextInt(0, 1)]);
        payload.setSupervisor(supervisor.getId());

        ArrayList<FieldDescriptor> a = new ArrayList<>(Arrays.asList(apiResponseFd));
        a.addAll(Arrays.asList(employeeFd));
        FieldDescriptor[] responseFds = new FieldDescriptor[a.size()];
        responseFds = a.toArray(responseFds);

        RestAssured.given()
                .spec(requestSpecification)
                .contentType("application/json")
                .accept("application/json")
                .body(gson.toJson(payload))
                .filter(document("employees-controller/create-employee", requestFields(createEmployeeFd),
                        responseFields(responseFds)))
                .when()
                .port(8080)
                .log().all()
                .when()
                .post("/employees")
                .then()
                .log().all()
                .assertThat().statusCode(is(201));
    }

    @Test
    public void givenAnEmployee_whenUpdateEmployee_thenReturnStatusOkAndUpdatedEmployee() {

        UpdateEmployeeDto payload = new UpdateEmployeeDto();
        payload.setId(employee.getId());
        payload.setFirstName(faker.name().firstName());
        payload.setLastName(faker.name().lastName());
        payload.setEmail(faker.internet().emailAddress());
        payload.setPhone("233" + faker.random().nextInt(244000000, 259000000));
        payload.setCountryCode("GH");
        payload.setRole(Role.values()[faker.random().nextInt(0, 1)]);
        payload.setSupervisor(supervisor.getId());

        ArrayList<FieldDescriptor> a = new ArrayList<>(Arrays.asList(apiResponseFd));
        a.addAll(Arrays.asList(employeeFd));

        FieldDescriptor[] fieldDescriptors = new FieldDescriptor[a.size()];
        fieldDescriptors = a.toArray(fieldDescriptors);

        RestAssured.given()
                .spec(requestSpecification)
                .contentType("application/json")
                .accept("application/json")
                .body(gson.toJson(payload))
                .filter(document("employees-controller/update-employee", requestFields(updateEmployeeFd),
                        responseFields(fieldDescriptors)))
                .when()
                .port(8080)
                .log().all()
                .when()
                .put("/employees")
                .then()
                .log().all()
                .assertThat().statusCode(is(200));
    }

    public void getEmployeeForTest() {
        employee = RestAssured.given()
                .spec(requestSpecification)
                .contentType("application/json")
                .accept("application/json")
                .when()
                .port(8080)
                .log().all()
                .when()
                .get("/employees/two")
                .jsonPath().getObject("data", EmployeeDto.class);
    }

    public void fetchRandomSupervisorForTest() {
        supervisor = RestAssured.given()
                .spec(requestSpecification)
                .contentType("application/json")
                .accept("application/json")
                .when()
                .port(8080)
                .log().all()
                .when()
                .get("/employees/one")
                .jsonPath().getObject("data", EmployeeDto.class);
    }
}