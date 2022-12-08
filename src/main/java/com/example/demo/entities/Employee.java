package com.example.demo.entities;

import com.example.demo.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Document("employees")
public class Employee implements Serializable {

    @Id
    private String id;

    @NotNull
    private Role role;

    //@NotNull
    private String supervisor;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String phone;

    @NotNull
    private Date createdAt;

    @NotNull
    private Date updatedAt;

    @DocumentReference
    private List<Leave> leaves;

    @DocumentReference
    private List<LeaveRequest> leaveRequests;
}