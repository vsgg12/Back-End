package com.example.mdmggreal.member.dto.response;

import com.example.mdmggreal.global.response.BaseResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@SuperBuilder
public class MobileCheckResponse extends BaseResponse {
    private String token;

    public static MobileCheckResponse from(String token, HttpStatus status) {
        return MobileCheckResponse.builder()
                .resultCode(status.value())
                .resultMsg(status.name())
                .token(token)
                .build();
    }
}
