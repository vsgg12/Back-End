package com.example.mdmggreal.global.response;

import com.example.mdmggreal.global.exception.ErrorCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@SuperBuilder
public class BaseResponse {
    private Integer resultCode;
    private String resultMsg;

    public static BaseResponse from(ErrorCode e) {
        return BaseResponse.builder()
                .resultCode(e.getHttpStatus().value())
                .resultMsg(e.getMessage())
                .build();
    }

    public static ResponseEntity<BaseResponse> toResponseEntity(HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .body(BaseResponse.builder()
                        .resultCode(httpStatus.value())
                        .resultMsg(httpStatus.name())
                        .build());
    }

    public static ResponseEntity<BaseResponse> toResponseEntity(HttpStatus httpStatus, String msg) {
        return ResponseEntity.status(httpStatus)
                .body(BaseResponse.builder()
                        .resultCode(httpStatus.value())
                        .resultMsg(msg)
                        .build());
    }

    public static ResponseEntity<BaseResponse> toResponseEntity(ErrorCode e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(BaseResponse.builder()
                        .resultCode(e.getHttpStatus().value())
                        .resultMsg(e.getMessage())
                        .build());
    }

}