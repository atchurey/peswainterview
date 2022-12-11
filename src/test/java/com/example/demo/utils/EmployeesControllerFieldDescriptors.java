package com.example.demo.utils;

import org.springframework.restdocs.payload.FieldDescriptor;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class EmployeesControllerFieldDescriptors {

    public static final FieldDescriptor[] employeeFd = new FieldDescriptor[]{
            fieldWithPath("data.id").description("The employee Id"),
            fieldWithPath("data.first_name").description("Employee's first name"),
            fieldWithPath("data.last_name").description("Employee's last name"),
            fieldWithPath("data.email").description("Employee's email address"),
            fieldWithPath("data.phone").description("Employee's phone number"),
            fieldWithPath("data.role").description("Employee's role"),
            fieldWithPath("data.supervisor").description("Primary id of the employee's assigned supervisor(also an employee)").optional(),
            fieldWithPath("data.created_at").description("When the employee was made"),
            fieldWithPath("data.updated_at").description("When the employee was last updated"),
    };

    public static final FieldDescriptor[] employeeListFd = new FieldDescriptor[]{
            fieldWithPath("data.data[0].id").description("The employee Id"),
            fieldWithPath("data.data[0].first_name").description("Employee's first name").optional(),
            fieldWithPath("data.data[0].last_name").description("Employee's last name"),
            fieldWithPath("data.data[0].email").description("Employee's email address"),
            fieldWithPath("data.data[0].phone").description("Employee's phone number"),
            fieldWithPath("data.data[0].role").description("Employee's role"),
            fieldWithPath("data.data[0].supervisor").description("Primary id of the employee's assigned supervisor(also an employee)"),
            fieldWithPath("data.data[0].created_at").description("When the employee was created"),
            fieldWithPath("data.data[0].updated_at").description("When the employee was last updated"),
    };

    public static final FieldDescriptor[] createEmployeeFd = new FieldDescriptor[]{
            fieldWithPath("first_name").description("Employee's first name"),
            fieldWithPath("last_name").description("Employee's last name"),
            fieldWithPath("email").description("Employee's email address"),
            fieldWithPath("phone").description("Employee's phone number"),
            fieldWithPath("country_code").description("Country code of the employee's provided phone number"),
            fieldWithPath("role").description("Employee's role"),
            fieldWithPath("supervisor").description("Primary id of the employee's assigned supervisor(also an employee)").optional(),
    };

    public static final FieldDescriptor[] updateEmployeeFd = new FieldDescriptor[]{
            fieldWithPath("id").description("Id of the employee to update"),
            fieldWithPath("first_name").description("Employee's first name"),
            fieldWithPath("last_name").description("Employee's last name"),
            fieldWithPath("email").description("Employee's email address"),
            fieldWithPath("phone").description("Employee's phone number"),
            fieldWithPath("country_code").description("Country code of the employee's provided phone number"),
            fieldWithPath("role").description("Employee's role"),
            fieldWithPath("supervisor").description("Primary id of the employee's assigned supervisor(also an employee)").optional(),
    };
}
