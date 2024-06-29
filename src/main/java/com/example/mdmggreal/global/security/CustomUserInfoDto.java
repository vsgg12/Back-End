package com.example.mdmggreal.global.security;

import com.example.mdmggreal.member.entity.Member;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomUserInfoDto{
    private Long memberId;

    // Member 객체로 생성
    public static CustomUserInfoDto of(Member member) {
        return CustomUserInfoDto.builder()
                .memberId(member.getId())
                .build();
    }

    // memberId 로 생성
    public static CustomUserInfoDto of(Long memberId) {
        return CustomUserInfoDto.builder()
                .memberId(memberId)
                .build();
    }
}
