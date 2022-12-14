package com.example.demo.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UpdateLeaveRequestDto implements Serializable {

    @NotNull
    private String id;
    private String reason;
    private String startAt;
    private String endAt;

}