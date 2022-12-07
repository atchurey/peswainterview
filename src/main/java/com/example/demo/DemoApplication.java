package com.example.demo;

import com.example.demo.configs.properties.AppProperties;
import com.example.demo.dtos.EmailPayloadDto;
import com.example.demo.dtos.SmsPayloadDto;
import com.example.demo.entities.Employee;
import com.example.demo.enums.SmsProvider;
import com.example.demo.notifications.channels.EmailChannel;
import com.example.demo.notifications.channels.SmsChannel;
import com.example.demo.notifications.messageclients.EmailClient;
import com.example.demo.notifications.messageclients.GiantSmsClient;
import com.example.demo.notifications.messageclients.TwilioClient;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.sql.Timestamp;
import java.util.Date;

@EnableAsync
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private LeaveRequestRepository leaveRequestRepository;
	@Autowired
	private SmsChannel smsChannel;
	@Autowired
	private EmailChannel emailChannel;
	@Autowired
	private EmailClient emailClient;
	@Autowired
	private TwilioClient twilioClient;
	@Autowired
	private GiantSmsClient giantSmsClient;
	@Autowired
	private AppProperties appProperties;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		employeeRepository.deleteAll();
		employeeRepository.deleteAll();

		// save a couple of employees
		Employee employee1 = new Employee();
		employee1.setId("firstemployee");
		employee1.setFirstName("First");
		employee1.setLastName("Employee");
		employee1.setEmail("first@gmail.com");
		employee1.setPhone("233540597186");
		employee1.setCreatedAt(new Date());

		Employee employee2 = new Employee();
		employee2.setId("secondemployee");
		employee2.setFirstName("Second");
		employee2.setLastName("Employee");
		employee2.setEmail("second@gmail.com");
		employee2.setPhone("233248529145");
		employee2.setCreatedAt(new Date());

		employeeRepository.save(employee1);
		employeeRepository.save(employee2);

		// fetch all customers
		System.out.println("Employees found with findAll():");
		System.out.println("-------------------------------");
		for (Employee employee : employeeRepository.findAll()) {
			System.out.println(employee);
		}
		System.out.println();

		// fetch an individual customer
		System.out.println("Employee found with findById('firstemployee'):");
		System.out.println("--------------------------------");
		System.out.println(employeeRepository.findById("firstemployee"));

		System.out.println("Employees found with findByLastName('Employee'):");
		System.out.println("--------------------------------");
		for (Employee employee : employeeRepository.findByLastName("Employee")) {
			System.out.println(employee);
		}


		/// Send sms
		SmsPayloadDto smsPayload = new SmsPayloadDto();
		smsChannel.process(smsPayload);

		SmsProvider providerToUser = SmsProvider.getByCode(appProperties.getGiantSmsApiBaseUrl());
		if (SmsProvider.TWILIO == providerToUser) {
			smsChannel.sendMessage(twilioClient);
		} else {
			smsChannel.sendMessage(giantSmsClient);
		}

		// Send email
		EmailPayloadDto emailPayload = new EmailPayloadDto();
		emailChannel.process(emailPayload);
		emailChannel.sendMessage(emailClient);

	}




}
