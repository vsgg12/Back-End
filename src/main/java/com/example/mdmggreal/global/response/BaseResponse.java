package com.example.mdmggreal.global.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@SuperBuilder
@Setter
public class BaseResponse {
    private Integer resultCode;
    private String resultMsg;

    public static BaseResponse from(HttpStatus httpStatus) {
        return BaseResponse.builder()
                .resultCode(httpStatus.value())
                .resultMsg(httpStatus.name())
                .build();
    }
}