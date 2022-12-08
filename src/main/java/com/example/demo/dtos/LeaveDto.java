package com.example.demo.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class LeaveDto implements Serializable {

    private String id;
    private String approvedBy;
    public String reason;
    private String startAt;
    private String endAt;
    private String approvedAt;
    private String createdAt;
    private EmployeeDto employee;

}