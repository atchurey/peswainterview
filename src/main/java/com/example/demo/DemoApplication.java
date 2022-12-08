package com.example.demo;

import com.example.demo.configs.properties.AppProperties;
import com.example.demo.dtos.EmailPayloadDto;
import com.example.demo.dtos.SmsPayloadDto;
import com.example.demo.entities.Employee;
import com.example.demo.entities.LeaveRequest;
import com.example.demo.enums.Role;
import com.example.demo.enums.SmsProvider;
import com.example.demo.notifications.channels.EmailChannel;
import com.example.demo.notifications.channels.SmsChannel;
import com.example.demo.notifications.messageclients.EmailClient;
import com.example.demo.notifications.messageclients.GiantSmsClient;
import com.example.demo.notifications.messageclients.TwilioClient;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.LeaveRepository;
import com.example.demo.repositories.LeaveRequestRepository;
import com.example.demo.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@EnableAsync(proxyTargetClass=true)
@EnableScheduling
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(GiantSmsClient.class);

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private LeaveRequestRepository leaveRequestRepository;
	@Autowired
	private LeaveRepository leaveRepository;
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

		Date nowDate = new Date();
		LocalDate nowLocalDate = LocalDate.now(ZoneId.systemDefault());

		employeeRepository.deleteAll();
		leaveRequestRepository.deleteAll();
		leaveRepository.deleteAll();

		// save a couple of employees
		Employee employee1 = new Employee();
		employee1.setId("one");
		employee1.setFirstName("Victor");
		employee1.setLastName("Somuah");
		employee1.setEmail("victor@gmail.com");
		employee1.setPhone("233241500723");
		employee1.setRole(Role.SUPERVISOR);
		employee1.setSupervisor(null);
		employee1.setCreatedAt(nowDate);
		employee1.setUpdatedAt(nowDate);
		employee1 = employeeRepository.save(employee1);

		Employee employee2 = new Employee();
		employee2.setId("two");
		employee2.setFirstName("Norbert");
		employee2.setLastName("Aberor");
		employee2.setEmail("norbert@gmail.com");
		employee2.setPhone("233248529145");
		employee2.setRole(Role.EMPLOYEE);
		employee2.setSupervisor(employee1.getId());
		employee2.setCreatedAt(nowDate);
		employee2.setUpdatedAt(nowDate);
		employeeRepository.save(employee2);

		List<Employee> employees = Arrays.asList(employee1, employee2);

		for (int i = 0; i <= 10; i++) {
			LeaveRequest leaveRequest = new LeaveRequest();
			leaveRequest.setId(String.valueOf(i));
			leaveRequest.setReason("Some reason " + i);
			leaveRequest.setCreatedAt(nowDate);
			leaveRequest.setUpdatedAt(nowDate);
			leaveRequest.setStartAt(nowLocalDate.plusDays(ThreadLocalRandom.current().nextInt(1, 20)));
			leaveRequest.setEndAt(nowLocalDate.plusDays(ThreadLocalRandom.current().nextInt(20, 100)));
			leaveRequest.setEmployee(employees.get(ThreadLocalRandom.current().nextInt(0, 2)));
			leaveRequest = leaveRequestRepository.save(leaveRequest);
			logger.info(">>> Saved LeaveRequest: {}", leaveRequest);

		}


		for (LeaveRequest leaveRequest : employeeRepository.findById("one").get().getLeaveRequests()) {
			logger.info(">>> Employee One LeaveRequest: {}", leaveRequest);
		}

		for (LeaveRequest leaveRequest : employeeRepository.findById("two").get().getLeaveRequests()) {
			logger.info(">>> Employee Two LeaveRequest: {}", leaveRequest);
		}


		/// Send sms
		/*SmsPayloadDto smsPayload = new SmsPayloadDto();
		smsPayload.setFrom(appProperties.getSendSmsAs());
		smsPayload.setTitle("New Message");
		smsPayload.setMessage("Hello");
		smsPayload.setToPhoneNumbers(Collections.singletonList("233540597186"));
		smsChannel.process(smsPayload);*/

		/*SmsProvider providerToUser = SmsProvider.getByCode(appProperties.getGiantSmsApiBaseUrl());
		if (SmsProvider.TWILIO == providerToUser) {
			//smsChannel.sendMessage(twilioClient);
		} else {
			//smsChannel.sendMessage(giantSmsClient);
		}*/

		// Send email
		/*EmailPayloadDto emailPayload = new EmailPayloadDto();
		emailPayload.setSubject("New Email");
		emailPayload.setToEmails(Collections.singletonList("atchureyalbert@gmail.com"));
		emailPayload.setMessage("Hello");*/

		/*emailChannel.process(emailPayload);
		//emailChannel.sendMessage(emailClient);*/

	}




}
