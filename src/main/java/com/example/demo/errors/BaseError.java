package com.example.demo.errors;

import lombok.Data;

@Data
public class BaseError {

    private String url;
    private int errorCode;
    private String errorMessage;
    private String userErrorMessage;

}
