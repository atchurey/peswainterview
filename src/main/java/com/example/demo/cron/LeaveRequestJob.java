package com.example.demo.cron;

import com.example.demo.entities.Employee;
import com.example.demo.entities.LeaveRequest;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.LeaveRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LeaveRequestJob {
    private static final Logger logger = LoggerFactory.getLogger(LeaveRequestJob.class);

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private EmployeeRepository repository;
    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Scheduled(cron = "0/10 * * * * ?")// Every 30 seconds
    //@Scheduled(cron = "${com.example.demo.system.config.leaveRequestCronInterval}")
    public void run()  {
        //logger.info(">>> LeaveRequestJob.run() called");

        Date now = new Date();

        Query query = new Query(Criteria.where("endAt").gt(now));
        mongoTemplate.stream(query, LeaveRequest.class)
                        .forEach(leaveRequest -> {
                            //logger.info(">>> Processing LeaveRequest: {}", leaveRequest);
                        });

        //logger.info(">>> LeaveRequestJob.run() finished");

    }
}
