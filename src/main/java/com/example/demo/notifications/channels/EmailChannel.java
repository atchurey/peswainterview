package com.example.demo.notifications.channels;

import com.example.demo.domain.MessagePayload;
import com.example.demo.dtos.EmailPayloadDto;
import com.example.demo.interfaces.MessageChannel;
import com.example.demo.interfaces.MessageClient;
import org.springframework.stereotype.Component;

@Component
public class EmailChannel implements MessageChannel {

    private MessagePayload messagePayload;

    @Override
    public void process(Object emailPayload) {

        try {
            if (emailPayload instanceof EmailPayloadDto payload) {

                messagePayload = new MessagePayload();
                messagePayload.setTitle(payload.getSubject());
                messagePayload.setMessage(payload.getMessage());
                messagePayload.setTo(payload.getToEmails());
                messagePayload.setEmailAttachments(payload.getAttachments());
            }
        } catch (Exception ex) {
            onFailure(ex);
        }
    }

    @Override
    public void sendMessage(MessageClient emailClient) {
        try {
            emailClient.send(messagePayload);
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
