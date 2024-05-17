package com.example.mdmggreal.member.dto.response;

import com.example.mdmggreal.global.response.BaseResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@SuperBuilder
public class NicknameCheckResponse extends BaseResponse {
    private Boolean nicknameCheck;

    public static NicknameCheckResponse of(HttpStatus status, Boolean nicknameCheck) {
        return NicknameCheckResponse.builder()
                .resultCode(status.value())
                .resultMsg(status.name())
                .nicknameCheck(nicknameCheck)
                .build();
    }
}
