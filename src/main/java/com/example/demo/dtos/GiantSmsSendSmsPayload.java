package com.example.demo.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class GiantSmsSendSmsPayload implements Serializable {
    private String from;
    private String to;
    private String msg;
}