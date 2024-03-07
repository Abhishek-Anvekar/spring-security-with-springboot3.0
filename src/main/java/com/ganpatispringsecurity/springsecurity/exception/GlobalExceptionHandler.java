package com.ganpatispringsecurity.springsecurity.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadApiRequestException.class)
    ResponseEntity<ApiResponseMessage> handleBadApiRequest(BadApiRequestException exception){
        logger.info("Bad Api Request !!");
        ApiResponseMessage apiResponseMessage = new ApiResponseMessage();
        apiResponseMessage.setMessage(exception.getMessage());
        apiResponseMessage.setSuccess(false);
        apiResponseMessage.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiResponseMessage,HttpStatus.BAD_REQUEST);
    }
}
