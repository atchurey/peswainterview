package com.example.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateEmployeeDto implements Serializable {

    @NotBlank
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String countryCode;

}