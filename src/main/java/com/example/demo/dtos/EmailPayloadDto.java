package com.example.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class EmailPayloadDto implements Serializable {
    @NotEmpty
    @NotBlank
    @Size(max = 150, min = 2)
    private String subject;
    @NotEmpty
    @NotBlank
    private String message;
    @NotEmpty
    private List<Object> toEmails;
    private Map<String, String> attachments;
}
