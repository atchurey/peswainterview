package com.example.demo.notifications.messageclients;

import com.example.demo.domain.MessagePayload;
import com.example.demo.dtos.GiantSmsSendSmsPayload;
import com.example.demo.dtos.GiantSmsSendSmsResponse;
import com.example.demo.interfaces.MessageClient;
import com.example.demo.network.ApiRequest;
import com.example.demo.network.repositories.GiantSmsApiRepository;
import com.twilio.exception.TwilioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.function.BiConsumer;

@Component
public class GiantSmsClient implements MessageClient {

    private static final Logger logger = LoggerFactory.getLogger(GiantSmsClient.class);

    @Autowired
    private GiantSmsApiRepository apiRepository;

    @Async("smsExecutor")
    @Override
    public void send(MessagePayload messagePayload) throws TwilioException {

        GiantSmsSendSmsPayload payload = new GiantSmsSendSmsPayload();
        payload.setFrom(StringUtils.isEmpty(messagePayload.getFrom()) ? "BillPrompt" :  messagePayload.getFrom());
        payload.setMsg(messagePayload.getMessage());

        messagePayload.getTo().forEach(to -> {
            payload.setTo((String) to);
            apiRepository.sendSms(payload, giantSmsSendSmsHandler());
        });
    }

    @Override
    public void onFailure(Object object) {
        //TODO what do we do in the event of failure?
    }

    private BiConsumer<Object, ApiRequest.Status> giantSmsSendSmsHandler(/*Sms sms*/) {

        return (result, apiStatus) -> {
            if (apiStatus == ApiRequest.Status.Success) {
                GiantSmsSendSmsResponse response = (GiantSmsSendSmsResponse) result;
                if (response.isStatus()) {
                    logger.error("GiantSms Sent: " + result);

                } else {
                }
            } else if (apiStatus == ApiRequest.Status.Error) {
                logger.error("GiantSms Error: " + result);
                //onFailure(sms);
            } else if (apiStatus == ApiRequest.Status.Failed) {
                logger.error("GiantSms Error: " + result);
                //onFailure(sms);
            } else {
                logger.error("GiantSms Error: Unknown Error");
                //onFailure(sms);
            }
        };
    }
}
