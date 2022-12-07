package com.example.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SmsPayloadDto implements Serializable {
    @NotEmpty
    @NotBlank
    @Size(max = 150, min = 2)
    private String title;
    @NotEmpty
    @NotBlank
    @Size(max = 500, min = 2)
    private String message;
    @NotEmpty
    @NotBlank
    @Size(max = 80, min = 2)
    private String from;
    @NotEmpty
    private List<Object> toPhoneNumbers;
}
