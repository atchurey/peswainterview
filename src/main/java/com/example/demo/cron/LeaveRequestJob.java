package com.example.demo.cron;

import com.example.demo.configs.properties.AppProperties;
import com.example.demo.dtos.EmailPayloadDto;
import com.example.demo.dtos.SmsPayloadDto;
import com.example.demo.entities.Employee;
import com.example.demo.entities.LeaveRequest;
import com.example.demo.enums.LeaveStatus;
import com.example.demo.enums.SmsProvider;
import com.example.demo.exceptions.ServiceException;
import com.example.demo.interfaces.MessageClient;
import com.example.demo.notifications.channels.EmailChannel;
import com.example.demo.notifications.channels.SmsChannel;
import com.example.demo.notifications.messageclients.EmailClient;
import com.example.demo.notifications.messageclients.GiantSmsClient;
import com.example.demo.notifications.messageclients.TwilioClient;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.LeaveRepository;
import com.example.demo.repositories.LeaveRequestRepository;
import com.example.demo.services.impl.LeaveRequestServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;

@Service
public class LeaveRequestJob {
    private static final Logger logger = LoggerFactory.getLogger(LeaveRequestJob.class);

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    @Autowired
    private LeaveRequestServiceImpl leaveRequestService;
    @Autowired
    private LeaveRepository leaveRepository;
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

    @Scheduled(cron = "0 0/2 * * * ?")// Every 2 minutes
    //@Scheduled(cron = "${com.example.demo.system.config.leaveRequestCronInterval}")
    public void run()  {
        logger.info(">>> LeaveRequestJob.run() called");

        LocalDate nowLocalDate = LocalDate.now(ZoneId.systemDefault());

        Query query = new Query(Criteria.where("endAt").gt(nowLocalDate));
        mongoTemplate.stream(query, LeaveRequest.class)
                        .forEach(leaveRequest -> {
                            try {
                                logger.info(">>> Processing LeaveRequest: {}", leaveRequest);

                                long daysToStartDate = Duration.between(nowLocalDate.atStartOfDay(), leaveRequest.getStartAt().atStartOfDay()).toDays();

                                if (daysToStartDate > 3) {
                                    //// Notify supervisor
                                    try {
                                        SmsProvider providerToUser = SmsProvider.getByCode(appProperties.getSmsClientToUse());
                                        MessageClient smsClientToUse;
                                        if (SmsProvider.TWILIO == providerToUser) {
                                            smsClientToUse = twilioClient;
                                        } else {
                                            smsClientToUse = giantSmsClient;
                                        }

                                        ////// Notify Supervisor
                                        Employee supervisor = employeeRepository.findById(leaveRequest.getEmployee().getSupervisor())
                                                .orElseThrow(() -> new ServiceException(100, "Supervisor not found"));

                                        String supervisorNotification = leaveRequest.getEmployee().getFirstName() + " " + leaveRequest.getEmployee().getLastName()
                                                + " has a leave request pending your attention.";

                                        // Sms notification
                                        SmsPayloadDto smsPayload = new SmsPayloadDto();
                                        smsPayload.setFrom(appProperties.getSendSmsAs());
                                        smsPayload.setTitle("Pending Leave Request");
                                        smsPayload.setMessage(supervisorNotification);
                                        smsPayload.setToPhoneNumbers(Collections.singletonList(supervisor.getPhone()));
                                        smsChannel.process(smsPayload);
                                        smsChannel.sendMessage(smsClientToUse);

                                        // Email notification
                                        EmailPayloadDto emailPayload = new EmailPayloadDto();
                                        emailPayload.setSubject("Pending Leave Request");
                                        emailPayload.setToEmails(Collections.singletonList(supervisor.getEmail()));
                                        emailPayload.setMessage(supervisorNotification);
                                        emailChannel.process(emailPayload);
                                        emailChannel.sendMessage(emailClient);
                                    } catch (Exception ex) {
                                        logger.info(">>> Error sending supervisor notification with {} days to start date: {}", daysToStartDate, ex.getMessage());
                                    }
                                } else {
                                    try {
                                        Employee supervisor = employeeRepository.findById(leaveRequest.getEmployee().getSupervisor())
                                                .orElseThrow(() -> new ServiceException(100, "Supervisor not found"));

                                        leaveRequestService.approveLeaveRequest(leaveRequest, supervisor, LeaveStatus.AUTO_APPROVED);
                                    } catch (Exception ex) {
                                        logger.info(">>> Error auto approving leave request with {} days to start date: {}", daysToStartDate, ex.getMessage());
                                    }
                                }
                            } catch (Exception ex) {
                                logger.info(">>> Error processing leave request: {}", leaveRequest);
                            }
                        });

        logger.info(">>> LeaveRequestJob.run() finished");
    }
}
