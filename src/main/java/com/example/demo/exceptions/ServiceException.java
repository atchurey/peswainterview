package com.example.demo.exceptions;

public class ServiceException extends RuntimeException {

    private int code;
    private String message;
    private String userMessage;
    private Exception exception;

    public ServiceException(int code, String message){
        this.code = code;
        this.message = message;
    }

    public ServiceException(int code, String message, String userMessage){
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
    }

    public ServiceException(int code, String message, String userMessage, Exception exception){
        this.code = code;
        this.message = message;
        this.userMessage = userMessage;
        this.exception = exception;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
