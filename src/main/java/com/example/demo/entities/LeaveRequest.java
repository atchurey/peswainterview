package com.example.demo.entities;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Data
@Document("leave_requests")
public class LeaveRequest implements Serializable {

    @Id
    public String id;

    public String reason;

    @NotNull
    private LocalDate startAt;

    @NotNull
    private LocalDate endAt;

    @NotNull
    private Date createdAt;

    @NotNull
    private Date updatedAt;

    @DocumentReference(lazy=true)
    private Employee employee;
}