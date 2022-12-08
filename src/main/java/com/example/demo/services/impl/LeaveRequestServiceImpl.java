package com.example.demo.services.impl;

import com.example.demo.dtos.CreateLeaveRequestDto;
import com.example.demo.dtos.LeaveRequestDto;
import com.example.demo.dtos.UpdateLeaveRequestDto;
import com.example.demo.entities.LeaveRequest;
import com.example.demo.exceptions.ServiceException;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.LeaveRequestRepository;
import com.example.demo.services.LeaveRequestService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {
    private final Logger logger = LoggerFactory.getLogger(LeaveRequestServiceImpl.class);

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;

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
        return null;
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
