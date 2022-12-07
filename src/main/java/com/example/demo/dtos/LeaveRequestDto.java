package com.example.demo.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LeaveRequestDto implements Serializable {

    public String id;
    private String approvedBy;
    public String reason;
    private boolean deleted;
    private Date startAt;
    private Date endAt;
    private Date deletedAt;
    private Date approvedAt;
    private Date createdAt;
    private EmployeeDto employee;

}