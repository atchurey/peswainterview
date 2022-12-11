package com.example.demo.utils;

import org.springframework.restdocs.payload.FieldDescriptor;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class CommonFieldDescriptors {

    public static final FieldDescriptor[] apiResponseFd = new FieldDescriptor[]{
            fieldWithPath("status_code").description("The response status code."),
            fieldWithPath("status_message").description("The response message."),
            fieldWithPath("data").description("The requested resource."),
            fieldWithPath("error").description("The error message."),};

    public static final FieldDescriptor[] pagedContentFd = new FieldDescriptor[]{
            fieldWithPath("data.total_elements").description("Total number of resource elements returnable"),
            fieldWithPath("data.total_pages").description("Total page returnable"),
            fieldWithPath("data.page").description("Current page"),
            fieldWithPath("data.size").description("Number of resources in page"),
            fieldWithPath("data.has_next_page").description("Has next page?"),
            fieldWithPath("data.has_previous_page").description("Has previous page?"),
            fieldWithPath("data.is_first").description("Is first page?"),
            fieldWithPath("data.is_last").description("Is last page?"),
            fieldWithPath("data.data").description("The requested resource list"),};

    public static final FieldDescriptor[] resourceErrorFd = new FieldDescriptor[]{
            fieldWithPath("timestamp").description("The time the error was encountered."),
            fieldWithPath("status").description("The status of the request."),
            fieldWithPath("message").description("The error message."),
            fieldWithPath("path").description("The request URL."),
            fieldWithPath("error").description("The HTTP error message."),};

}
