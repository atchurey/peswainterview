package com.example.demo.dtos;

import com.example.demo.enums.LeaveStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class LeaveDto implements Serializable {

    private String id;
    private LeaveStatus status;
    private String attendedToBy;
    private String reason;
    private String startAt;
    private String endAt;
    private String attendedToAt;
    private String createdAt;
    private EmployeeDto employee;

}