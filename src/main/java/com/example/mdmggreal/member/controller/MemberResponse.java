package com.example.mdmggreal.member.controller;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.member.dto.MemberDTO;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@SuperBuilder
public class MemberResponse extends BaseResponse {
    private MemberDTO memberDTO;

    public static MemberResponse of(HttpStatus status, MemberDTO memberDTO) {
        return MemberResponse.builder()
                .resultCode(status.value())
                .resultMsg(status.name())
                .memberDTO(memberDTO)
                .build();

    }
}

