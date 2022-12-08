package com.example.demo.services.impl;

import com.example.demo.dtos.CreateLeaveRequestDto;
import com.example.demo.dtos.LeaveRequestDto;
import com.example.demo.dtos.UpdateLeaveRequestDto;
import com.example.demo.entities.Employee;
import com.example.demo.entities.LeaveRequest;
import com.example.demo.exceptions.ServiceException;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.LeaveRequestRepository;
import com.example.demo.services.EmployeeService;
import com.example.demo.services.LeaveRequestService;
import com.example.demo.utils.Utils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {
    private final Logger logger = LoggerFactory.getLogger(LeaveRequestServiceImpl.class);

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;

    private final String dateFormat = "yyyy-MM-dd";

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    LeaveRequestServiceImpl(LeaveRequestRepository leaveRequestRepository,
                            EmployeeRepository employeeRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
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
        return null;
    }

    @Override
    public void deleteLeaveRequest(String id) {
        leaveRequestRepository.deleteById(id);
    }

}
