package com.example.demo.dtos;

import com.example.demo.enums.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EmployeeDto implements Serializable {

    private String id;
    private String firstName;
    private String lastName;
    private Role role;
    private String supervisor;
    private String createdAt;
    private String updatedAt;
    private List<LeaveRequestDto> leaveRequests;

}