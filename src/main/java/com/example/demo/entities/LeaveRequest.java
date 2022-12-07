package com.example.demo.entities;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serializable;
import java.util.Date;

@Data
@Document("leave_requests")
public class LeaveRequest implements Serializable {

    @Id
    public String id;

    public String reason;

    private boolean deleted;

    @NotNull
    private Date startAt;

    @NotNull
    private Date endAt;

    @NotNull
    private Date deletedAt;

    @NotNull
    private String approvedBy;

    @NotNull
    private Date approvedAt;

    @NotNull
    private Date createdAt;

    @DocumentReference(lazy=true)
    private Employee employee;
}