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
    @NotNull
    private LocalDate startAt;
    @NotNull
    private LocalDate endAt;

}