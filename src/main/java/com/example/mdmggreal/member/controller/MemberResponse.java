package com.example.mdmggreal.member.controller;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.member.dto.response.MemberProfileDTO;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@SuperBuilder
public class MemberResponse extends BaseResponse {
    private MemberProfileDTO memberProfileDTO;

    public static MemberResponse of(HttpStatus status, MemberProfileDTO memberProfileDTO) {
        return MemberResponse.builder()
                .resultCode(status.value())
                .resultMsg(status.name())
                .memberProfileDTO(memberProfileDTO)
                .build();

    }
}

