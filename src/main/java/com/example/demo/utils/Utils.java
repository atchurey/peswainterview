package com.example.demo.utils;

import com.example.demo.domain.ApiResponse;
import com.example.demo.domain.PagedContent;
import com.example.demo.enums.ResponseMessage;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

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

}
