package com.example.demo.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class LeaveRequestDto implements Serializable {

    private String id;
    private String reason;
    private String startAt;
    private String endAt;
    private String createdAt;
    private String updatedAt;
    private EmployeeDto employee;

}