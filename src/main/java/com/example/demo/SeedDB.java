package com.example.demo;

import com.example.demo.entities.Employee;
import com.example.demo.entities.LeaveRequest;
import com.example.demo.enums.Role;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.LeaveRepository;
import com.example.demo.repositories.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class SeedDB implements ApplicationRunner {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    @Autowired
    private LeaveRepository leaveRepository;

    @Override
    public void run(ApplicationArguments args) {

        // Seed db with two employees and two leave requests
		if (employeeRepository.count() <= 0) {

			Date nowDate = new Date();
			LocalDate nowLocalDate = LocalDate.now(ZoneId.systemDefault());

			Employee employee1 = new Employee();
			employee1.setId("one");
			employee1.setFirstName("Super");
			employee1.setLastName("Visor");
			employee1.setEmail("super@visor.com");
			employee1.setPhone("233540597186");
			employee1.setRole(Role.SUPERVISOR);
			employee1.setSupervisor("one");
			employee1.setCreatedAt(nowDate);
			employee1.setUpdatedAt(nowDate);
			employee1 = employeeRepository.save(employee1);

			Employee employee2 = new Employee();
			employee2.setId("two");
			employee2.setFirstName("Emplo");
			employee2.setLastName("Yee");
			employee2.setEmail("emplo@yee.com");
			employee2.setPhone("233241500723");
			employee2.setRole(Role.EMPLOYEE);
			employee2.setSupervisor(employee1.getId());
			employee2.setCreatedAt(nowDate);
			employee2.setUpdatedAt(nowDate);
			employeeRepository.save(employee2);

			List<Employee> employees = Arrays.asList(employee1, employee2);

			for (int i = 0; i <= 1; i++) {
				LeaveRequest leaveRequest = new LeaveRequest();
				leaveRequest.setId(String.valueOf(i));
				leaveRequest.setReason("Some reason " + i);
				leaveRequest.setCreatedAt(nowDate);
				leaveRequest.setUpdatedAt(nowDate);
				leaveRequest.setStartAt(nowLocalDate.plusDays(ThreadLocalRandom.current().nextInt(1, 20)));
				leaveRequest.setEndAt(nowLocalDate.plusDays(ThreadLocalRandom.current().nextInt(20, 100)));
				leaveRequest.setEmployee(employees.get(ThreadLocalRandom.current().nextInt(0, 2)));
				leaveRequestRepository.save(leaveRequest);
			}
		}
    }
}
