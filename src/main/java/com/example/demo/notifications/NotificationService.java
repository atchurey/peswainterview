package com.example.demo.notifications;

import com.example.demo.configs.properties.AppProperties;
import com.example.demo.dtos.EmailPayloadDto;
import com.example.demo.dtos.SmsPayloadDto;
import com.example.demo.enums.SmsProvider;
import com.example.demo.interfaces.MessageClient;
import com.example.demo.notifications.channels.EmailChannel;
import com.example.demo.notifications.channels.SmsChannel;
import com.example.demo.notifications.messageclients.EmailClient;
import com.example.demo.notifications.messageclients.GiantSmsClient;
import com.example.demo.notifications.messageclients.TwilioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class NotificationService {
    private final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private SmsChannel smsChannel;
    @Autowired
    private EmailChannel emailChannel;
    @Autowired
    private EmailClient emailClient;
    @Autowired
    private TwilioClient twilioClient;
    @Autowired
    private GiantSmsClient giantSmsClient;
    @Autowired
    private AppProperties appProperties;

    public void sendNotification(String title, String message, String phone, String email) {

        try {
            SmsProvider providerToUser = SmsProvider.getByCode(appProperties.getSmsClientToUse());
            MessageClient smsClientToUse;
            if (SmsProvider.TWILIO == providerToUser) {
                smsClientToUse = twilioClient;
            } else {
                smsClientToUse = giantSmsClient;
            }

            // Sms notification
            SmsPayloadDto smsPayload = new SmsPayloadDto();
            smsPayload.setFrom(appProperties.getSendSmsAs());
            smsPayload.setTitle(title);
            smsPayload.setMessage(message);
            smsPayload.setToPhoneNumbers(Collections.singletonList(phone));
            smsChannel.process(smsPayload);
            smsChannel.sendMessage(smsClientToUse);

            // Email notification
            EmailPayloadDto emailPayload = new EmailPayloadDto();
            emailPayload.setSubject(title);
            emailPayload.setToEmails(Collections.singletonList(email));
            emailPayload.setMessage(message);
            emailChannel.process(emailPayload);
            emailChannel.sendMessage(emailClient);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
