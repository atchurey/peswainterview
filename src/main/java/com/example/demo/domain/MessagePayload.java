package com.example.demo.domain;


import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class MessagePayload implements Serializable {

    private String fcmTopic;
    private String title;
    private String message;
    private String from;
    private String fcmData;
    private Map<String, String> emailAttachments;
    private List<Object> to;

}