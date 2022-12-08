package com.example.demo.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApproveLeaveRequestDto implements Serializable {

    @NotNull
    private String id;
    private String supervisor;
    private boolean approve;

}