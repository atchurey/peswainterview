package com.example.demo.dtos;

import com.example.demo.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateEmployeeDto implements Serializable {

    @NotBlank
    private String id;
    private Role role;
    private String supervisor;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String countryCode;

}