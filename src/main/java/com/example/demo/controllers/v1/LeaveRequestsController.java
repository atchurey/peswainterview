package com.example.demo.controllers.v1;

import com.example.demo.domain.ApiResponse;
import com.example.demo.domain.PagedContent;
import com.example.demo.dtos.CreateLeaveRequestDto;
import com.example.demo.dtos.LeaveRequestDto;
import com.example.demo.dtos.UpdateLeaveRequestDto;
import com.example.demo.entities.LeaveRequest;
import com.example.demo.services.LeaveRequestService;
import com.example.demo.utils.Utils;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/leave_requests")
public class LeaveRequestsController {
    private final Logger logger = LoggerFactory.getLogger(LeaveRequestsController.class);

    @Autowired
    private LeaveRequestService leaveRequestService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseBody
    public ApiResponse<PagedContent<LeaveRequestDto>> getAllLeaveRequests(Pageable pageable) {
        logger.info("HTTP Request: getAllLeaveRequests");
        Page<LeaveRequest> page = leaveRequestService.getAllLeaveRequests(pageable);

        List<LeaveRequestDto> dtos = page.stream().map(leaveRequest ->
                modelMapper.map(leaveRequest, LeaveRequestDto.class)).collect(Collectors.toList());

        ApiResponse<PagedContent<LeaveRequestDto>> response = Utils.wrapInPagedApiResponse(page, dtos);

        logger.info("HTTP Response: getAllLeaveRequests: {}", response);
        return response;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ApiResponse<LeaveRequestDto> getLeaveRequestById(@PathVariable String id) {
        logger.info("HTTP Request: getLeaveRequestById: {}", id);
        LeaveRequest leaveRequest = leaveRequestService.getLeaveRequestById(id);

        LeaveRequestDto dto = null;
        if (leaveRequest != null) {
            dto = modelMapper.map(leaveRequest, LeaveRequestDto.class);
        }

        ApiResponse<LeaveRequestDto> response = Utils.wrapInApiResponse(dto);

        logger.info("HTTP Response: getLeaveRequestById: {}", response);
        return response;
    }

    @PostMapping()
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<LeaveRequestDto> createLeaveRequest(@Valid @RequestBody CreateLeaveRequestDto payload) {
        logger.info("HTTP REQUEST: createLeaveRequest: {}", payload);

        LeaveRequestDto dto = leaveRequestService.createLeaveRequest(payload);

        ApiResponse<LeaveRequestDto> response = Utils.wrapInApiResponse(dto);

        logger.info("HTTP RESPONSE: createLeaveRequest: {}", response);
        return response;
    }

    @PutMapping()
    @ResponseBody
    public ApiResponse<LeaveRequestDto> updateLeaveRequest(@Valid @RequestBody UpdateLeaveRequestDto payload) {
        logger.info("HTTP REQUEST: updateLeaveRequest: {}", payload);
        LeaveRequestDto dto = leaveRequestService.updateLeaveRequest(payload);

        ApiResponse<LeaveRequestDto> response = Utils.wrapInApiResponse(dto);

        logger.info("HTTP RESPONSE: updateLeaveRequest: {}", response);
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ApiResponse<LeaveRequestDto> deleteLeaveRequestById(@PathVariable String id){
        logger.info("HTTP REQUEST: deleteLeaveRequestById: {}", id);
        leaveRequestService.deleteLeaveRequest(id);

        ApiResponse<LeaveRequestDto> response = Utils.wrapInApiResponse(null);

        logger.info("HTTP RESPONSE: deleteLeaveRequestById: {}", response);
        return response;
    }

}