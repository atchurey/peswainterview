package com.example.demo.utils;

import org.springframework.restdocs.payload.FieldDescriptor;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

public class LeaveRequestsControllerFieldDescriptors {

    public static final FieldDescriptor[] leaveRequestFd = new FieldDescriptor[]{
            fieldWithPath("data.id").description("The leave request Id"),
            fieldWithPath("data.reason").description("Reason for applying for the leave"),
            fieldWithPath("data.start_at").description("The date on which leave begins"),
            fieldWithPath("data.end_at").description("The date on which the leave ends"),
            fieldWithPath("data.created_at").description("When the leave request was made"),
            fieldWithPath("data.updated_at").description("When the leave request was last updated"),
            subsectionWithPath("data.employee").description("The employee who applied for the leave"),
    };

    public static final FieldDescriptor[] leaveRequestListFd = new FieldDescriptor[]{
            fieldWithPath("data.data[0].id").description("The leave request Id"),
            fieldWithPath("data.data[0].reason").description("Reason for applying for leave").optional(),
            fieldWithPath("data.data[0].start_at").description("The date on which leave begins"),
            fieldWithPath("data.data[0].end_at").description("The date on which the leave ends"),
            fieldWithPath("data.data[0].created_at").description("When the leave request was made"),
            fieldWithPath("data.data[0].updated_at").description("When the leave request was last updated"),
            fieldWithPath("data.data[0].employee").description("The employee who applied for the leave")
    };

    public static final FieldDescriptor[] createLeaveRequestFd = new FieldDescriptor[]{
            fieldWithPath("employee_id").description("Primary id of the employee requesting for leave"),
            fieldWithPath("reason").description("Reason for applying for leave").optional(),
            fieldWithPath("start_at").description("The date on which leave begins"),
            fieldWithPath("end_at").description("The date on which the leave ends"),
    };

    public static final FieldDescriptor[] updateLeaveRequestFd = new FieldDescriptor[]{
            fieldWithPath("id").description("Primary id of the leave request to update"),
            fieldWithPath("reason").description("Reason for applying for leave").optional(),
            fieldWithPath("start_at").description("The date on which leave begins"),
            fieldWithPath("end_at").description("The date on which the leave ends"),
    };

}
