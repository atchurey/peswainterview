package com.example.demo.utils;

import com.example.demo.domain.ApiResponse;
import com.example.demo.domain.PagedContent;
import com.example.demo.enums.ResponseMessage;
import com.example.demo.exceptions.ServiceException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static <T> ApiResponse<T> wrapInApiResponse(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatusCode(ResponseMessage.SUCCESS.getCode());
        response.setStatusMessage(ResponseMessage.SUCCESS.toString());
        response.setData(data);
        return response;
    }

    public static <T, W> ApiResponse<PagedContent<W>> wrapInPagedApiResponse(Page page, List<W> data) {
        ApiResponse<PagedContent<W>> response = new ApiResponse<>();
        response.setStatusCode(ResponseMessage.SUCCESS.getCode());
        response.setStatusMessage(ResponseMessage.SUCCESS.toString());
        response.setData(new PagedContent(page, data));
        return response;
    }

    public static <W, T> List<W> mapToList(Page<T> page, Class<W> tClass, ModelMapper mapper) {
        return page.stream().map(t ->
                mapper.map(t, tClass)).collect(Collectors.toList());
    }

    public static <W, T> W map(T entity, Class<W> dtoClass, ModelMapper mapper) {
        W dto = null;
        if (entity != null) {
            dto = mapper.map(entity, dtoClass);
        }
        return dto;
    }

    public static String plusDays(final String date, final String dateFormat, int daysToAdd) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
            LocalDate inputDate = LocalDate.parse(date, dtf);
            return dtf.format(inputDate.plusDays(daysToAdd)) ;
        } catch (Exception ex) {
            throw new ServiceException(100, ex.getMessage());
        }
    }

    public static boolean isDatePast(final String date, final String dateFormat) {
        try {
            LocalDate localDate = LocalDate.now(ZoneId.systemDefault());

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
            LocalDate inputDate = LocalDate.parse(date, dtf);

            return inputDate.isBefore(localDate);
        } catch (Exception ex) {
            throw new ServiceException(100, ex.getMessage());
        }
    }

    public static boolean isDateFuture(final String date, final String dateFormat) {
        try {
            LocalDate localDate = LocalDate.now(ZoneId.systemDefault());

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
            LocalDate inputDate = LocalDate.parse(date, dtf);

            return inputDate.isAfter(localDate);
        } catch (Exception ex) {
            throw new ServiceException(100, ex.getMessage());
        }
    }

    public static String internationalizePhoneNumber(String countryCode, String phone) throws Exception {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(phone, countryCode);
        return phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL).replace(" ", "").replace("+", "");
    }

}
