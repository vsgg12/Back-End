package com.example.mdmggreal.global.exception;

import com.example.mdmggreal.global.response.BaseResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
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

    // RequestParam
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMsg = ex.getMessage();
        return BaseResponse.toResponseEntity(BAD_REQUEST, errorMsg);
    }

    // RequestParam
    // 잘못된 파라미터 타입에 대한 처리 (예: long 타입의 파라미터인데 string을 보낸 경우)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMsg = String.format("잘못된 요청: 파라미터 '%s' 값 '%s'는 잘못된 형식입니다.", ex.getName(), ex.getValue());
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

    // 호출자가 인수로 부적절한 값을 넘길 때 발생
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return BaseResponse.toResponseEntity(BAD_REQUEST, ex.getMessage());
    }

}
