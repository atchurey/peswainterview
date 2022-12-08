package com.example.demo.services;

import com.example.demo.entities.Leave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeaveService {

    Leave getLeaveById(String id);
    Page<Leave> getAllLeaves(Pageable pageable);

}
