package com.example.demo.configs;

import com.example.demo.dtos.EmployeeDto;
import com.example.demo.dtos.LeaveDto;
import com.example.demo.dtos.LeaveRequestDto;
import com.example.demo.entities.Employee;
import com.example.demo.entities.Leave;
import com.example.demo.entities.LeaveRequest;
import org.modelmapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class DtoToEntityMapper {

    @Bean
    public ModelMapper modelMapper() {

        PropertyMap<Employee, EmployeeDto> employeeMap = new PropertyMap<>() {
            protected void configure() {
                skip().setLeaveRequests(null);
            }
        };

        PropertyMap<LeaveRequest, LeaveRequestDto> leaveRequestMap = new PropertyMap<>() {
            protected void configure() {
            }
        };

        PropertyMap<Leave, LeaveDto> leaveMap = new PropertyMap<>() {
            protected void configure() {
            }
        };

        Converter<Date, String> timestampStringConverter = new AbstractConverter<>() {
            protected String convert(Date source) {

                DateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                return dateFormatUTC.format(source);
            }
        };

        Converter<String, Date> stringTimeStampConverter = new AbstractConverter<>() {
            protected Date convert(String source) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                try {
                    return dateFormat.parse(source);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(employeeMap);
        modelMapper.addMappings(leaveRequestMap);
        modelMapper.addMappings(leaveMap);

        modelMapper.addConverter(timestampStringConverter);
        modelMapper.addConverter(stringTimeStampConverter);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        return modelMapper;
    }
}
