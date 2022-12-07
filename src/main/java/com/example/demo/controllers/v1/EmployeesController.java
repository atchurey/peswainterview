package com.example.demo.controllers.v1;

import com.example.demo.domain.ApiResponse;
import com.example.demo.dtos.EmployeeDto;
import com.example.demo.domain.PagedContent;
import com.example.demo.entities.Employee;
import com.example.demo.services.EmployeeService;
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
@RequestMapping(path = "/employees")
public class EmployeesController {
    private final Logger logger = LoggerFactory.getLogger(EmployeesController.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/search")
    @ResponseBody
    public ApiResponse<PagedContent<EmployeeDto>> searchEmployees(Pageable pageable) {
        logger.info("HTTP Request: searchEmployees");
        Page<Employee> page = employeeService.getAllEmployees(pageable);

        List<EmployeeDto> dtos = page.stream().map(employee ->
                modelMapper.map(employee, EmployeeDto.class)).collect(Collectors.toList());

        ApiResponse<PagedContent<EmployeeDto>> response = Utils.wrapInPagedApiResponse(page, dtos);

        logger.info("HTTP Response: searchEmployees: {}", response);
        return response;
    }

    @GetMapping
    @ResponseBody
    public ApiResponse<PagedContent<EmployeeDto>> getAllEmployees(Pageable pageable) {
        logger.info("HTTP Request: getAllEmployees");
        Page<Employee> page = employeeService.getAllEmployees(pageable);

        List<EmployeeDto> dtos = page.stream().map(employee ->
                modelMapper.map(employee, EmployeeDto.class)).collect(Collectors.toList());

        ApiResponse<PagedContent<EmployeeDto>> response = Utils.wrapInPagedApiResponse(page, dtos);

        logger.info("HTTP Response: getAllEmployees: {}", response);
        return response;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ApiResponse<EmployeeDto> getEmployeeById(@PathVariable String id) {
        logger.info("HTTP Request: getEmployeeById: {}", id);
        Employee employee = employeeService.getEmployeeById(id);

        EmployeeDto dto = null;
        if (employee != null) {
            dto = modelMapper.map(employee, EmployeeDto.class);
        }

        ApiResponse<EmployeeDto> response = Utils.wrapInApiResponse(dto);

        logger.info("HTTP Response: getEmployeeById: {}", response);
        return response;
    }

    @PostMapping()
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto payload) {
        logger.info("HTTP REQUEST: createEmployee: {}", payload);

        EmployeeDto dto = employeeService.createEmployee(payload);

        ApiResponse<EmployeeDto> response = Utils.wrapInApiResponse(dto);

        logger.info("HTTP RESPONSE: createEmployee: {}", response);
        return response;
    }

    @PutMapping()
    @ResponseBody
    public ApiResponse<EmployeeDto> updateEmployee(@Valid @RequestBody EmployeeDto payload) {
        logger.info("HTTP REQUEST: updateEmployee: {}", payload);
        EmployeeDto dto = employeeService.updateEmployee(payload);

        ApiResponse<EmployeeDto> response = Utils.wrapInApiResponse(dto);

        logger.info("HTTP RESPONSE: updateEmployee: {}", response);
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ApiResponse<EmployeeDto> deleteEmployeeById(@PathVariable String id){
        logger.info("HTTP REQUEST: deleteEmployeeById: {}", id);
        employeeService.deleteEmployee(id);

        ApiResponse<EmployeeDto> response = Utils.wrapInApiResponse(null);

        logger.info("HTTP RESPONSE: deleteEmployeeById: {}", response);
        return response;
    }

}