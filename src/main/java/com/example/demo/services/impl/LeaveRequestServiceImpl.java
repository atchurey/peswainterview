package com.example.demo.services.impl;

import com.example.demo.configs.properties.AppProperties;
import com.example.demo.dtos.*;
import com.example.demo.entities.Employee;
import com.example.demo.entities.Leave;
import com.example.demo.entities.LeaveRequest;
import com.example.demo.enums.LeaveStatus;
import com.example.demo.enums.Role;
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
import com.example.demo.services.LeaveRequestService;
import com.example.demo.utils.Utils;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {
    private final Logger logger = LoggerFactory.getLogger(LeaveRequestServiceImpl.class);

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    private final String dateFormat = "yyyy-MM-dd";

    @Autowired
    private ModelMapper modelMapper;
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

    @Autowired
    LeaveRequestServiceImpl(LeaveRequestRepository leaveRequestRepository,
                            LeaveRepository leaveRepository,
                            EmployeeRepository employeeRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public LeaveRequest getLeaveRequestById(String employeeId) {
        return leaveRequestRepository.findById(employeeId).orElseThrow(() -> new ServiceException(100, "LeaveRequest not found."));
    }

    @Override
    public Page<LeaveRequest> getAllLeaveRequests(Pageable pageable) {
        return leaveRequestRepository.findAll(pageable);
    }

    @Override
    public LeaveRequestDto createLeaveRequest(CreateLeaveRequestDto payload) {

        Employee employee = employeeRepository.findById(payload.getEmployeeId())
                .orElseThrow(() -> new ServiceException(100, "Employee not found"));

        Date now = new Date();
        LocalDate nowLocalDate = LocalDate.now(ZoneId.systemDefault());
        LocalDate startAt = Utils.toLocalDate(payload.getStartAt(), dateFormat);
        LocalDate endAt = Utils.toLocalDate(payload.getEndAt(), dateFormat);

        if (startAt.isBefore(nowLocalDate) || endAt.isBefore(nowLocalDate)) {
            throw new ServiceException(100, "Start and end dates cannot be in the past");
        }

        if (endAt.isBefore(startAt)) {
            throw new ServiceException(100, "Invalid start and end dates");
        }

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setReason(payload.getReason());
        leaveRequest.setCreatedAt(now);
        leaveRequest.setUpdatedAt(now);
        leaveRequest.setStartAt(startAt);
        leaveRequest.setEndAt(endAt);
        leaveRequest.setEmployee(employee);
        leaveRequest = leaveRequestRepository.save(leaveRequest);

        return modelMapper.map(leaveRequest, LeaveRequestDto.class);
    }

    @Override
    public LeaveRequestDto updateLeaveRequest(UpdateLeaveRequestDto payload) {
        LeaveRequest leaveRequest = getLeaveRequestById(payload.getId());

        Date now = new Date();
        LocalDate nowLocalDate = LocalDate.now(ZoneId.systemDefault());

        LocalDate startAt;
        if (Strings.isNotBlank(payload.getStartAt())) {
            startAt = Utils.toLocalDate(payload.getStartAt(), dateFormat);

            if (startAt.isBefore(nowLocalDate)) {
                throw new ServiceException(100, "Start date cannot be in the past");
            }
        } else {
            startAt = leaveRequest.getStartAt();
        }

        LocalDate endAt;
        if (Strings.isNotBlank(payload.getEndAt())) {
            endAt = Utils.toLocalDate(payload.getEndAt(), dateFormat);

            if (endAt.isBefore(nowLocalDate)) {
                throw new ServiceException(100, "End date cannot be in the past");
            }
        } else {
            endAt = leaveRequest.getEndAt();
        }

        if (endAt.isBefore(startAt)) {
            throw new ServiceException(100, "Invalid start and end dates");
        }

        leaveRequest.setReason(payload.getReason());
        leaveRequest.setStartAt(startAt);
        leaveRequest.setEndAt(endAt);
        leaveRequest.setUpdatedAt(now);
        leaveRequest = leaveRequestRepository.save(leaveRequest);

        return modelMapper.map(leaveRequest, LeaveRequestDto.class);
    }

    @Override
    public LeaveDto approveLeaveRequest(ApproveLeaveRequestDto payload) {

        LeaveRequest leaveRequest = getLeaveRequestById(payload.getId());
        Employee employee = leaveRequest.getEmployee();

        Employee supervisor = employeeRepository.findById(payload.getSupervisor())
                .orElseThrow(() -> new ServiceException(100, "Supervisor not found"));

        if (Role.SUPERVISOR != supervisor.getRole()) {
            throw new ServiceException(100, "Only employees with SUPERVISOR role can perform this action.");
        }

        if (Strings.isNotBlank(employee.getSupervisor())
                && !employee.getSupervisor().equals(supervisor.getId())) {
            throw new ServiceException(100, "Only am employee's with supervisor can manage the employees leave requests.");
        }

        Date now = new Date();

        Leave leave = new Leave();
        leave.setStatus(payload.isApprove() ? LeaveStatus.APPROVED : LeaveStatus.DISAPPROVED);
        leave.setAttendedToBy(supervisor.getId());
        leave.setReason(leaveRequest.getReason());
        leave.setStartAt(leaveRequest.getStartAt());
        leave.setEndAt(leaveRequest.getEndAt());
        leave.setCreatedAt(now);
        leave.setUpdatedAt(now);
        leave.setAttendedToAt(now);
        leave.setEmployee(employee);
        leave = leaveRepository.save(leave);

        // Delete all approved or disapproved LeaveRequests
        // This should help manage the size of the LeaveRequests collection
        // This should positively impact performance of the LeaveRequestJob
        leaveRequestRepository.deleteById(leaveRequest.getId());

        ///// Send notifications
        try {
            SmsProvider providerToUser = SmsProvider.getByCode(appProperties.getGiantSmsApiBaseUrl());
            MessageClient smsClientToUse;
            if (SmsProvider.TWILIO == providerToUser) {
                smsClientToUse = twilioClient;
            } else {
                smsClientToUse = giantSmsClient;
            }

            ////// Notify Employee
            String employeeNotification = "Your leave request was "
                    + leave.getStatus().name().toLowerCase()
                    + "by "
                    + supervisor.getFirstName() + " " + supervisor.getLastName();

            // Sms notification
            SmsPayloadDto smsPayload = new SmsPayloadDto();
            smsPayload.setFrom(appProperties.getSendSmsAs());
            smsPayload.setTitle("Leave Approval");
            smsPayload.setMessage(employeeNotification);
            smsPayload.setToPhoneNumbers(Collections.singletonList(employee.getLeaveRequests()));
            smsChannel.process(smsPayload);
            smsChannel.sendMessage(smsClientToUse);

            // Email notification
            EmailPayloadDto emailPayload = new EmailPayloadDto();
            emailPayload.setSubject("New Email");
            emailPayload.setToEmails(Collections.singletonList("atchureyalbert@gmail.com"));
            emailPayload.setMessage("Hello");
            emailChannel.process(emailPayload);
            emailChannel.sendMessage(emailClient);

            ////// Notify supervisor
            String supervisorNotification = "You "
                    + leave.getStatus().name().toLowerCase()
                    + " a leave request from"
                    + employee.getFirstName() + " " + employee.getLastName();

            // Sms notification
            smsPayload = new SmsPayloadDto();
            smsPayload.setFrom(appProperties.getSendSmsAs());
            smsPayload.setTitle("Leave Approval");
            smsPayload.setMessage(supervisorNotification);
            smsPayload.setToPhoneNumbers(Collections.singletonList(supervisor.getLeaveRequests()));
            smsChannel.process(smsPayload);
            smsChannel.sendMessage(smsClientToUse);

            // Email notification
            emailPayload = new EmailPayloadDto();
            emailPayload.setSubject("New Email");
            emailPayload.setToEmails(Collections.singletonList("atchureyalbert@gmail.com"));
            emailPayload.setMessage("Hello");
            emailChannel.process(emailPayload);
            emailChannel.sendMessage(emailClient);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return modelMapper.map(leave, LeaveDto.class);
    }

    @Override
    public void deleteLeaveRequest(String id) {
        if (leaveRequestRepository.existsById(id)) {
            leaveRequestRepository.deleteById(id);
        } else {
            throw new ServiceException(100, "LeaveRequest not found.");
        }
    }

}
