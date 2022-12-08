package com.example.demo.services;

import com.example.demo.dtos.*;
import com.example.demo.entities.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeaveRequestService {

    LeaveRequest getLeaveRequestById(String id);
    Page<LeaveRequest> getAllLeaveRequests(Pageable pageable);
    LeaveRequestDto createLeaveRequest(CreateLeaveRequestDto payload);
    LeaveRequestDto updateLeaveRequest(UpdateLeaveRequestDto payload);
    LeaveDto approveLeaveRequest(ApproveLeaveRequestDto payload);
    void deleteLeaveRequest(String id);

}
