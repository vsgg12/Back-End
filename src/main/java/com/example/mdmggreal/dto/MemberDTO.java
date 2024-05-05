package com.example.mdmggreal.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberDTO {

    /** 네이버_토큰_ID  */
    private String id;
    /** 이메일  */
    private String email;
    /** 닉네임  */
    private String nickname;
    /** 전화번호  */
    private String mobileNumber;
    /** 프로필사진  */
    private String profileImage;
    /** 나이  */
    private Integer age;

}
