package com.example.demo.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EmployeeDto implements Serializable {

    private String id;
    private String firstName;
    private String lastName;
    private String createdAt;
    private String updatedAt;
    private List<LeaveRequestDto> leaveRequests;

}