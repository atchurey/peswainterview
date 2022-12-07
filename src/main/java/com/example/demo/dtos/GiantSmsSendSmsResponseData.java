package com.example.demo.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class GiantSmsSendSmsResponseData implements Serializable {
    private String messageId;
    private String scheduleDate;
    private int rate;
    private String status;
    private String reasons;
    private String lastUpdatedDate;
}