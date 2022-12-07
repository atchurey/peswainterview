package com.example.demo.dtos;

import com.example.demo.entities.LeaveRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EmployeeDto implements Serializable {

    private String id;
    private String firstName;
    private String lastName;
    private List<LeaveRequestDto> leaveRequests;

}