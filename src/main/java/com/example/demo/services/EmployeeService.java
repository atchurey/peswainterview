package com.example.demo.services;

import com.example.demo.dtos.CreateEmployeeDto;
import com.example.demo.dtos.EmployeeDto;
import com.example.demo.dtos.UpdateEmployeeDto;
import com.example.demo.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    Employee getEmployeeById(String id);
    Page<Employee> getAllEmployees(Pageable pageable);
    EmployeeDto createEmployee(CreateEmployeeDto payload);
    EmployeeDto updateEmployee(UpdateEmployeeDto payload);
}
