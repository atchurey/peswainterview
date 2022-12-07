package com.example.demo.interfaces;

import com.example.demo.domain.MessagePayload;

public interface MessageClient {
    void send(MessagePayload messagePayload) throws Exception;
    void onFailure(Object object) throws Exception;
}
