package com.example.demo.dtos;

import com.example.demo.entities.Employee;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Data
public class LeaveDto implements Serializable {

    public String id;
    private String approvedBy;
    public String reason;
    private LocalDate startAt;
    private LocalDate endAt;
    private Date approvedAt;
    private Date createdAt;
    private EmployeeDto employee;

}