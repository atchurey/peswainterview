package com.example.demo.notifications.channels;

import com.example.demo.domain.MessagePayload;
import com.example.demo.dtos.SmsPayloadDto;
import com.example.demo.interfaces.MessageChannel;
import com.example.demo.interfaces.MessageClient;
import org.springframework.stereotype.Component;

@Component
public class SmsChannel implements MessageChannel {

    private MessagePayload messagePayload;

    @Override
    public void process(Object smsPayload) {
        try {
            if (smsPayload instanceof SmsPayloadDto payload) {

                messagePayload = new MessagePayload();
                messagePayload.setTitle(payload.getTitle());
                messagePayload.setMessage(payload.getMessage());
                messagePayload.setFrom(payload.getFrom());
                messagePayload.setTo(payload.getToPhoneNumbers());
            }
        } catch (Exception ex) {
            onFailure(ex);
        }
    }

    @Override
    public void sendMessage(MessageClient smsClient) {
        try {
            smsClient.send(messagePayload);
        } catch (Exception ex) {
            onFailure(ex);
        }
    }

    @Override
    public void onFailure(Exception ex) {
        ex.printStackTrace();
        // TODO what happens when something fails
    }
}
