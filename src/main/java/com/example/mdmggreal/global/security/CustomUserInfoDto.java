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
    private String email;
    private String nickname;
    private String mobile;


    public static CustomUserInfoDto of(Member member) {
        return CustomUserInfoDto.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .mobile(member.getMobile())
                .build();
    }
}
