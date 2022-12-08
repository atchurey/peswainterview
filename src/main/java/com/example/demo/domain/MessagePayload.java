package com.example.demo.domain;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MessagePayload implements Serializable {

    private String title;
    private String message;
    private String from;
    private List<Object> to;

}