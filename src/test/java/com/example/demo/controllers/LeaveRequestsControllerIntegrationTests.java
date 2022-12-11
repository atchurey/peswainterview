package com.example.demo.controllers;

import static com.example.demo.utils.CommonFieldDescriptors.*;
import static com.example.demo.utils.LeaveRequestsControllerFieldDescriptors.*;
import com.example.demo.controllers.v1.LeaveRequestsController;
import com.example.demo.dtos.*;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.LeaveRepository;
import com.example.demo.repositories.LeaveRequestRepository;
import com.example.demo.services.impl.LeaveRequestServiceImpl;
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
@WebMvcTest(LeaveRequestsController.class)
public class LeaveRequestsControllerIntegrationTests {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @MockBean
    private LeaveRequestRepository leaveRequestRepository;
    @MockBean
    private LeaveRepository leaveRepository;
    @MockBean
    private EmployeeRepository employeeRepository;
    @MockBean
    private LeaveRequestServiceImpl leaveRequestService;
    @MockBean
    private ModelMapper modelMapper;
    private RequestSpecification requestSpecification;

    private Gson gson;

    private Faker faker;

    private EmployeeDto supervisor = null;
    private EmployeeDto employee = null;
    private LeaveRequestDto leaveRequestDto = null;
    private LeaveDto leaveDto;

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
        fetchRandomLeaveRequestForTest();
        faker = new Faker();
    }

    @Test
    public void givenThereAreLeaveRequests_whenGetAllLeaveRequests_thenReturnStatusOkAndPagedLeaveRequests() throws Exception {

        ArrayList<FieldDescriptor> a = new ArrayList<>(Arrays.asList(apiResponseFd));
        a.addAll(Arrays.asList(pagedContentFd));
        a.addAll(Arrays.asList(leaveRequestListFd));

        FieldDescriptor[] fieldDescriptors = new FieldDescriptor[a.size()];
        fieldDescriptors = a.toArray(fieldDescriptors);

        RestAssured.given()
                .spec(requestSpecification)
                .contentType("application/json")
                .accept("application/json")
                .filter(document("leave-requests-controller/get-all-leave-requests",
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
                .get("/leave_requests?page=0&size=1")
                .then()
                .log().all()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void givenALeaveRequestExists_whenGetLeaveRequestById_thenReturnStatusOkAndTheLeaveRequest() throws Exception {

        ArrayList<FieldDescriptor> a = new ArrayList<>(Arrays.asList(apiResponseFd));
        a.addAll(Arrays.asList(leaveRequestFd));

        FieldDescriptor[] fieldDescriptors = new FieldDescriptor[a.size()];
        fieldDescriptors = a.toArray(fieldDescriptors);

        RestAssured.given()
                .spec(requestSpecification)
                .contentType("application/json")
                .accept("application/json")
                .filter(document("leave-requests-controller/get-leave-request-by-id", responseFields(
                        fieldDescriptors)))
                .when()
                .port(8080)
                .log().all()
                .when()
                .get("/leave_requests/" + leaveRequestDto.getId())
                .then()
                .log().all()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void givenALeaveRequest_whenCreateLeaveRequest_thenReturnStatusOkAndCreatedLeaveRequest() {

        CreateLeaveRequestDto payload = new CreateLeaveRequestDto();
        payload.setEmployeeId("");
        payload.setReason("");
        payload.setStartAt("");
        payload.setEndAt("");

        ArrayList<FieldDescriptor> a = new ArrayList<>(Arrays.asList(apiResponseFd));
        a.addAll(Arrays.asList(leaveRequestFd));
        FieldDescriptor[] responseFds = new FieldDescriptor[a.size()];
        responseFds = a.toArray(responseFds);

        RestAssured.given()
                .spec(requestSpecification)
                .contentType("application/json")
                .accept("application/json")
                .body(gson.toJson(payload))
                .filter(document("leave-requests-controller/create-leave-request", requestFields(createLeaveRequestFd),
                        responseFields(responseFds)))
                .when()
                .port(8080)
                .log().all()
                .when()
                .post("/leave_requests")
                .then()
                .log().all()
                .assertThat().statusCode(is(201));
    }

    @Test
    public void givenALeaveRequest_whenUpdateLeaveRequest_thenReturnStatusOkAndUpdatedLeaveRequest() {

        UpdateLeaveRequestDto editBranchDto = new UpdateLeaveRequestDto();


        ArrayList<FieldDescriptor> a = new ArrayList<>(Arrays.asList(apiResponseFd));
        a.addAll(Arrays.asList(leaveRequestFd));

        FieldDescriptor[] fieldDescriptors = new FieldDescriptor[a.size()];
        fieldDescriptors = a.toArray(fieldDescriptors);

        RestAssured.given()
                .spec(requestSpecification)
                .contentType("application/json")
                .accept("application/json")
                .body(gson.toJson(editBranchDto))
                .filter(document("leave-requests-controller/update-leave-request", requestFields(updateLeaveRequestFd),
                        responseFields(fieldDescriptors)))
                .when()
                .port(8080)
                .log().all()
                .when()
                .put("/leave_requests")
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

    public void fetchRandomLeaveRequestForTest() {
        leaveRequestDto = RestAssured.given()
                .spec(requestSpecification)
                .contentType("application/json")
                .accept("application/json")
                .when()
                .port(8080)
                .log().all()
                .when()
                .get("/leave_requests?page=0&size=1&sort=createdAt,desc")
                .jsonPath().getObject("data.data[0]", LeaveRequestDto.class);
    }

}