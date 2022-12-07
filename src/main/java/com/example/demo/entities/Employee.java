package com.example.demo.entities;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Document("employees")
public class Employee implements Serializable {

    @Id
    public String id;

    @NotNull
    public String firstName;

    @NotNull
    public String lastName;

    @NotNull
    public String email;

    @NotNull
    public String phone;

    @NotNull
    private Date createdAt;

    @DocumentReference
    private List<LeaveRequest> leaveRequests;
}