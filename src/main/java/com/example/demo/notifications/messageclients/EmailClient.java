package com.example.demo.notifications.messageclients;

import com.example.demo.configs.properties.AppProperties;
import com.example.demo.domain.MessagePayload;
import com.example.demo.exceptions.ServiceException;
import com.example.demo.interfaces.MessageClient;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailClient implements MessageClient {

    private static final Logger logger = LoggerFactory.getLogger(EmailClient.class);

    @Autowired
    private AppProperties appProperties;
    @Autowired
    private JavaMailSender emailSender;

    @Async("emailExecutor")
    @Override
    public void send(MessagePayload messagePayload) throws ServiceException {

        try {
            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            List<String> tos = messagePayload.getTo().stream()
                    .map(to -> (String) to).toList();

            helper.setFrom(appProperties.getSendEmailAs());
            helper.setTo(tos.get(0));
            helper.setText(messagePayload.getMessage());
            helper.setSubject(messagePayload.getTitle());

            emailSender.send(message);
        } catch (ServiceException ex ) {
            ex.printStackTrace();
            throw  ex;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onFailure(Object object) throws Exception {
        //TODO what do we do in the event of failure?
    }
}
