package com.example.demo.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Data
public class CreateLeaveRequestDto implements Serializable {

    @NotNull
    public String employeeId;
    public String reason;
    @NotNull
    private LocalDate startAt;
    @NotNull
    private LocalDate endAt;

}