package com.example.mdmggreal.member.dto;

import com.example.mdmggreal.oauth.OAuthAttributes;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
@Getter
public class MemberDTO  {

    /** 네이버_토큰_ID  */
    private String token;
    /** 이메일  */
    private String email;
    /** 닉네임  */
    private String nickname;
    /** 연령대  */
    private String age;
    /** 성별  */
    private String gender;
    /** 전화번호  */
    private String mobile;
    /** 프로필사진  */
    private String profileImage;
    /** 인증여부 */
    private boolean agreeAge;
    private boolean agreeTerms;
    private boolean agreePrivacy;
    private boolean agreePromotion;

    public static MemberDTO from(OAuthAttributes attributes) {
        return MemberDTO.builder()
                .token(attributes.getToken())
                .age(attributes.getAge())
                .gender(attributes.getGender())
                .mobile(attributes.getMobile())
                .email(attributes.getEmail())
                .nickname(attributes.getNickname())
                .build();
    }
}
