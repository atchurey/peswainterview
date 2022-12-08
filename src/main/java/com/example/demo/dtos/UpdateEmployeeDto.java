package com.example.demo.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateEmployeeDto implements Serializable {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

}