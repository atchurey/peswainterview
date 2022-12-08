package com.example.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Data
public class CreateLeaveRequestDto implements Serializable {

    @NotBlank
    private String employeeId;
    private String reason;
    @NotBlank
    private String startAt;
    @NotBlank
    private String endAt;

}