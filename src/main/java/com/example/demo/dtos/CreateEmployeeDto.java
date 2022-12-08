package com.example.demo.dtos;

import com.example.demo.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateEmployeeDto implements Serializable {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private Role role;
    private String supervisor;
    @Email
    private String email;
    @NotBlank
    private String phone;
    @NotBlank
    private String countryCode;

}