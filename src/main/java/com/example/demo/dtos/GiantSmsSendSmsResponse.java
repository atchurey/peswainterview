package com.example.demo.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class GiantSmsSendSmsResponse implements Serializable {

    private boolean status;
    private String message;
    private GiantSmsSendSmsResponseData data;
}