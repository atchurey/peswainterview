package com.example.demo.services.impl;

import com.example.demo.dtos.CreateEmployeeDto;
import com.example.demo.dtos.EmployeeDto;
import com.example.demo.dtos.UpdateEmployeeDto;
import com.example.demo.entities.Employee;
import com.example.demo.exceptions.ServiceException;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.services.EmployeeService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private final EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee getEmployeeById(String employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new ServiceException(100, "Employee not found."));
    }

    @Override
    public Page<Employee> getAllEmployees(Pageable pageable) {
        //return employeeRepository.findAll(spec, pageable);
        return employeeRepository.findAll(pageable);
    }

    @Override
    public EmployeeDto createEmployee(CreateEmployeeDto payload) {
        return null;
    }

    @Override
    public EmployeeDto updateEmployee(UpdateEmployeeDto payload) {
        return null;
    }

    @Override
    public void deleteEmployee(String employeeId) {

        Employee employee = getEmployeeById(employeeId);
        //employee.setDeleted(true);
        //employee.setDeletedAt(new Timestamp(new Date().getTime()));
        employeeRepository.save(employee);

    }

}
