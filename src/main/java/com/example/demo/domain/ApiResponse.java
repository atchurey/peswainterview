package com.example.demo.domain;

import com.example.demo.errors.BaseError;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApiResponse<T> implements Serializable {

    private int statusCode;
    private String statusMessage;
    private T data;
    private BaseError error;

}