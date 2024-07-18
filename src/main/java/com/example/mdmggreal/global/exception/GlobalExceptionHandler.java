package com.example.mdmggreal.global.exception;

import com.example.mdmggreal.global.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<BaseResponse> handleCustomException(CustomException e) {
        return BaseResponse.toResponseEntity(e.getErrorCode());
    }

    // RequestPart
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<BaseResponse> handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        String errorMsg = ex.getRequestPartName() + "을(를) 확인해주세요.";
        return BaseResponse.toResponseEntity(BAD_REQUEST, errorMsg);
    }

    // RequestHeader
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<BaseResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        String errorMsg = ex.getHeaderName() + "을(를) 확인해주세요.";
        return BaseResponse.toResponseEntity(BAD_REQUEST, errorMsg);
    }

    // RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMsg = Objects.requireNonNull(fieldError).getField() + "은(는) " + fieldError.getDefaultMessage();
        return BaseResponse.toResponseEntity(BAD_REQUEST, errorMsg);
    }
}
