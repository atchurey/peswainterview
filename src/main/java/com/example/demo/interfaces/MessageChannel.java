package com.example.demo.interfaces;

public interface MessageChannel {

    void process(Object payload);
    void sendMessage(MessageClient messageClient);
    void onFailure(Exception ex);
}
