package com.example.demo.services.impl;

import com.example.demo.entities.Leave;
import com.example.demo.exceptions.ServiceException;
import com.example.demo.repositories.LeaveRepository;
import com.example.demo.services.LeaveService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LeaveServiceImpl implements LeaveService {
    private final Logger logger = LoggerFactory.getLogger(LeaveServiceImpl.class);
    private final LeaveRepository leaveRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    LeaveServiceImpl(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    @Override
    public Leave getLeaveById(String leaveId) {
        return leaveRepository.findById(leaveId).orElseThrow(() -> new ServiceException(100, "Leave not found."));
    }

    @Override
    public Page<Leave> getAllLeaves(Pageable pageable) {
        return leaveRepository.findAll(pageable);
    }
}
