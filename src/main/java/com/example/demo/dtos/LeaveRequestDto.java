package com.example.demo.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class LeaveRequestDto implements Serializable {

    public String id;
    public String reason;
    private String startAt;
    private String endAt;
    private String createdAt;
    private EmployeeDto employee;

}