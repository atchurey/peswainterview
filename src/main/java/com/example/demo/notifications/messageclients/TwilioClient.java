package com.example.demo.notifications.messageclients;

import com.example.demo.configs.properties.AppProperties;
import com.example.demo.domain.MessagePayload;
import com.example.demo.interfaces.MessageClient;
import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TwilioClient implements MessageClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwilioClient.class);

    @Autowired
    private AppProperties appProperties;

    @Async("smsExecutor")
    @Override
    public void send(MessagePayload messagePayload) throws TwilioException {
        Twilio.init(appProperties.getTwilioAccountSID(), appProperties.getTwilioAuthToken());

        PhoneNumber originNumber;

        if (TextUtils.isEmpty(messagePayload.getFrom())) {
            originNumber = new PhoneNumber(appProperties.getTwilioPhoneNumber());
        } else {
            originNumber = new PhoneNumber(messagePayload.getFrom());
        }

        List<PhoneNumber> destinationNumbers = messagePayload.getTo().stream()
                .map(to -> new PhoneNumber((String) to)).toList();

        destinationNumbers
                .forEach(destinationNumber -> {
                    try {
                        LOGGER.info("Sending SMS to " + destinationNumber.toString());
                        Message.creator(destinationNumber, originNumber, messagePayload.getMessage())
                                .create();
                        LOGGER.info("Sent SMS to " + destinationNumber);
                    } catch (TwilioException ex) {
                        LOGGER.error("An exception occurred trying to send a messagePayload to {}, exception: {}", destinationNumber.toString(),
                                ex.getMessage());
                        onFailure(ex);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        onFailure(ex);
                    }
                });
    }

    @Override
    public void onFailure(Object object) {
        //TODO what do we do in the event of failure?
    }
}
