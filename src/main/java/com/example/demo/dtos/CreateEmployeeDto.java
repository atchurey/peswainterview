package com.example.demo.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CreateEmployeeDto implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

}