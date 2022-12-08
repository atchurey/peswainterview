package com.example.demo.controllers.v1;

import com.example.demo.domain.ApiResponse;
import com.example.demo.domain.PagedContent;
import com.example.demo.dtos.LeaveDto;
import com.example.demo.entities.Leave;
import com.example.demo.services.LeaveService;
import com.example.demo.utils.Utils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/leave")
public class LeaveController {
    private final Logger logger = LoggerFactory.getLogger(LeaveController.class);

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseBody
    public ApiResponse<PagedContent<LeaveDto>> getAllLeaves(Pageable pageable) {
        logger.info("HTTP Request: getAllLeaves");
        Page<Leave> page = leaveService.getAllLeaves(pageable);

        List<LeaveDto> dtos = page.stream().map(leave ->
                modelMapper.map(leave, LeaveDto.class)).collect(Collectors.toList());

        ApiResponse<PagedContent<LeaveDto>> response = Utils.wrapInPagedApiResponse(page, dtos);

        logger.info("HTTP Response: getAllLeaves: {}", response);
        return response;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ApiResponse<LeaveDto> getLeaveById(@PathVariable String id) {
        logger.info("HTTP Request: getLeaveById: {}", id);
        Leave leave = leaveService.getLeaveById(id);

        LeaveDto dto = null;
        if (leave != null) {
            dto = modelMapper.map(leave, LeaveDto.class);
        }

        ApiResponse<LeaveDto> response = Utils.wrapInApiResponse(dto);

        logger.info("HTTP Response: getLeaveById: {}", response);
        return response;
    }
}