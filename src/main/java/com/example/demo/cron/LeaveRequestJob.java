package com.example.demo.cron;

import com.example.demo.entities.Employee;
import com.example.demo.entities.LeaveRequest;
import com.example.demo.enums.LeaveStatus;
import com.example.demo.exceptions.ServiceException;
import com.example.demo.notifications.NotificationService;
import com.example.demo.repositories.EmployeeRepository;
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

@Service
public class LeaveRequestJob {
    private static final Logger logger = LoggerFactory.getLogger(LeaveRequestJob.class);

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private LeaveRequestServiceImpl leaveRequestService;
    @Autowired
    private NotificationService notificationService;

    @Scheduled(cron = "${com.example.demo.system.config.leaveRequestCronInterval}")
    public void run() {
        logger.info(">>> LeaveRequestJob.run() called");

        LocalDate nowLocalDate = LocalDate.now(ZoneId.systemDefault());

        Query query = new Query(Criteria.where("endAt").gt(nowLocalDate));
        mongoTemplate.stream(query, LeaveRequest.class)
                .forEach(leaveRequest -> {
                    try {
                        logger.info(">>> Processing LeaveRequest: {}", leaveRequest);

                        long daysToStartDate = Duration.between(nowLocalDate.atStartOfDay(), leaveRequest.getStartAt().atStartOfDay()).toDays();

                        if (daysToStartDate > 3) {

                            //Notify supervisor
                            Employee supervisor = employeeRepository.findById(leaveRequest.getEmployee().getSupervisor())
                                    .orElseThrow(() -> new ServiceException(100, "Supervisor not found"));

                            String supervisorNotification = leaveRequest.getEmployee().getFirstName() + " " + leaveRequest.getEmployee().getLastName()
                                    + " has a leave request pending your attention.";

                            notificationService.sendNotification("Pending Leave Request",
                                    supervisorNotification,
                                    supervisor.getPhone(),
                                    supervisor.getPhone()
                            );
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
