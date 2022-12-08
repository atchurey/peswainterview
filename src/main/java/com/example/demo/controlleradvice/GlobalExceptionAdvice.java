package com.example.demo.controlleradvice;

import com.example.demo.domain.ApiResponse;
import com.example.demo.errors.BaseError;
import com.example.demo.exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

    @ExceptionHandler(value = ServiceException.class)
    public final ResponseEntity<Object> handleServiceException(ServiceException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.OK;

        BaseError baseError = new BaseError();
        baseError.setUrl(getUrl(request));
        baseError.setErrorCode(ex.getCode());
        baseError.setErrorMessage(ex.getMessage());

        ApiResponse errorResponse = new ApiResponse();
        errorResponse.setStatusCode(100);
        errorResponse.setStatusMessage("failed");
        errorResponse.setError(baseError);

        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.CONFLICT;

        BaseError baseError = new BaseError();
        baseError.setUrl(getUrl(request));
        baseError.setErrorCode(HttpStatus.CONFLICT.value());
        baseError.setErrorMessage(ex.getMessage());

        return handleExceptionInternal(ex, baseError, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        BaseError baseError = new BaseError();
        baseError.setUrl(getUrl(request));
        baseError.setErrorCode(status.value());
        baseError.setErrorMessage(errors.toString());

        ApiResponse<String> errorResponse = new ApiResponse<>();
        errorResponse.setStatusCode(100);
        errorResponse.setStatusMessage("failed");
        errorResponse.setError(baseError);

        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    private ResponseEntity<Object> handleConstraintException(ConstraintViolationException ex, HttpHeaders headers, HttpStatus
            status, WebRequest webRequest) {

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach((error) -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
        });

        BaseError baseError = new BaseError();
        baseError.setUrl(getUrl(webRequest));
        baseError.setErrorCode(status.value());
        baseError.setErrorMessage(errors.toString());

        ApiResponse<String> errorResponse = new ApiResponse<>();
        errorResponse.setStatusCode(100);
        errorResponse.setStatusMessage("failed");
        errorResponse.setError(baseError);

        return handleExceptionInternal(ex, errorResponse, headers, status, webRequest);
    }


    private String getUrl(WebRequest webRequest){
        if (webRequest instanceof ServletWebRequest) {
            ServletWebRequest servletRequest = (ServletWebRequest) webRequest;
            HttpServletRequest request = servletRequest.getNativeRequest(HttpServletRequest.class);
            return request.getRequestURI();
        }
        return null;
    }

}
