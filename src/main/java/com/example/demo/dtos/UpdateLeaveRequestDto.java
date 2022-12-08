package com.example.demo.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UpdateLeaveRequestDto implements Serializable {

    @NotNull
    public String id;
    public String reason;
    @NotNull
    private LocalDate startAt;
    @NotNull
    private LocalDate endAt;

}