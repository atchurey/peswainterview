package com.example.demo.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LeaveRequestDto implements Serializable {

    public String id;
    public String reason;
    private Date startAt;
    private Date endAt;
    private Date createdAt;
    private EmployeeDto employee;

}