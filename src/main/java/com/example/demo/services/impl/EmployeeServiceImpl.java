package com.example.demo.services.impl;

import com.example.demo.dtos.CreateEmployeeDto;
import com.example.demo.dtos.EmployeeDto;
import com.example.demo.dtos.UpdateEmployeeDto;
import com.example.demo.entities.Employee;
import com.example.demo.enums.Role;
import com.example.demo.exceptions.ServiceException;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.services.EmployeeService;
import com.example.demo.utils.Utils;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

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
        return employeeRepository.findAll(pageable);
    }

    @Override
    public EmployeeDto createEmployee(CreateEmployeeDto payload) {

        try {
            payload.setPhone(Utils.internationalizePhoneNumber(payload.getCountryCode(), payload.getPhone()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(100, "Invalid phone number");
        }

        if (Strings.isNotBlank(payload.getSupervisor())) {

            Employee supervisor = employeeRepository.findById(payload.getSupervisor())
                    .orElseThrow(() -> new ServiceException(100, "Supervisor not found"));

            if (Role.SUPERVISOR != supervisor.getRole()) {
                throw new ServiceException(100, "Invalid employee provided as supervisor.");
            }
        }

        Date now = new Date();

        Employee employee = new Employee();
        employee.setRole(payload.getRole());
        employee.setSupervisor(payload.getSupervisor());
        employee.setFirstName(payload.getFirstName());
        employee.setLastName(payload.getLastName());
        employee.setEmail(payload.getEmail());
        employee.setPhone(payload.getPhone());
        employee.setCreatedAt(now);
        employee.setUpdatedAt(now);
        employee = employeeRepository.save(employee);

        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    public EmployeeDto updateEmployee(UpdateEmployeeDto payload) {

        Employee employee = getEmployeeById(payload.getId());

        if (Strings.isNotBlank(payload.getPhone()) && Strings.isNotBlank(payload.getCountryCode())) {
            try {
                payload.setPhone(Utils.internationalizePhoneNumber(payload.getCountryCode(), payload.getPhone()));
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(100, "Invalid phone number");
            }
        }

        if (Strings.isNotBlank(payload.getSupervisor())) {

            Employee supervisor = employeeRepository.findById(payload.getSupervisor())
                    .orElseThrow(() -> new ServiceException(100, "Supervisor not found"));

            if (Role.SUPERVISOR != supervisor.getRole()) {
                throw new ServiceException(100, "Invalid employee provided as supervisor.");
            }
        }

        employee.setRole(payload.getRole() == null ? employee.getRole() : payload.getRole());
        employee.setSupervisor(Strings.isBlank(payload.getSupervisor()) ? employee.getSupervisor() : payload.getSupervisor());
        employee.setFirstName(Strings.isBlank(payload.getFirstName()) ? employee.getFirstName() : payload.getFirstName());
        employee.setLastName(Strings.isBlank(payload.getLastName()) ? employee.getLastName() : payload.getLastName());
        employee.setEmail(Strings.isBlank(payload.getEmail()) ? employee.getEmail() : payload.getEmail());
        employee.setPhone(Strings.isBlank(payload.getPhone()) ? employee.getEmail() : payload.getEmail());
        employee.setUpdatedAt(new Date());

        employee = employeeRepository.save(employee);

        return modelMapper.map(employee, EmployeeDto.class);
    }

}
