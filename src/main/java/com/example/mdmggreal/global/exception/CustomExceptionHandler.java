package com.example.mdmggreal.global.exception;

import com.example.mdmggreal.global.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<BaseResponse> handleCustomException(CustomException e) {
        return BaseResponse.toResponseEntity(e.getErrorCode());
    }
}
