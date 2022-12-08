package com.example.demo.configs.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Data
@ConfigurationProperties("com.example.demo.system.config")
@Component
public class AppProperties implements Serializable {

    private String twilioPhoneNumber;
    private String twilioAccountSID;
    private String twilioAuthToken;
    private String giantSmsApiBaseUrl;
    private String giantSmsApiUsername;
    private String giantSmsApiPassword;
    private String smsClientToUse;
    private String sendEmailAs;
    private String sendSmsAs;
    private List<String> smsClients;

}