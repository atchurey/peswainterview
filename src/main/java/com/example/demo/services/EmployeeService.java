package com.example.demo.services;

import com.example.demo.dtos.EmployeeDto;
import com.example.demo.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    Employee getEmployeeById(String id);
    Page<Employee> getAllEmployees(Pageable pageable);
    EmployeeDto createEmployee(EmployeeDto createInvoiceDto);
    EmployeeDto updateEmployee(EmployeeDto editLocationDto);
    void deleteEmployee(String id);

}
