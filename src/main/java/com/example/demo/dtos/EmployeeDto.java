package com.example.demo.dtos;

import com.example.demo.enums.Role;
import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeDto implements Serializable {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String role;
    private String supervisor;
    private String createdAt;
    private String updatedAt;
}