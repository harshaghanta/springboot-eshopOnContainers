package com.eshoponcontainers.catalogapi.config;

import java.time.LocalDateTime;

import com.eshoponcontainers.catalogapi.excep.CatalogDomainException;
import com.eshoponcontainers.catalogapi.excep.CustomErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;



@ControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleCatalogExceptions(Exception ex, WebRequest request) {

        log.error("Exception caught in GlobalHandler", ex);
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setTimeStamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(ex.toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(((ServletWebRequest)request).getRequest().getRequestURI().toString());
        ResponseEntity<CustomErrorResponse> responseEntity = ResponseEntity.badRequest().body(errorResponse);
        return responseEntity;
    }
    
    @ExceptionHandler(CatalogDomainException.class)
    public ResponseEntity<CustomErrorResponse> handleCatalogException(Exception ex, WebRequest request) {

        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setTimeStamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(ex.toString());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(((ServletWebRequest)request).getRequest().getRequestURI().toString());
        ResponseEntity<CustomErrorResponse> responseEntity = ResponseEntity.badRequest().body(errorResponse);
        return responseEntity;
    }

   
    
}
