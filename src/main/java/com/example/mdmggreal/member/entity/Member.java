package com.example.mdmggreal.member.entity;

import com.example.mdmggreal.base.entity.BaseEntity;
import com.example.mdmggreal.member.dto.MemberDTO;
import com.example.mdmggreal.member.type.Agree;
import com.example.mdmggreal.member.type.Role;
import com.example.mdmggreal.oauth.OAuthAttributes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.example.mdmggreal.member.type.Role.USER;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /** 토큰  */
    private String token;
    /** 이메일  */
    @Column(nullable = false)
    private String email;
    /** 닉네임  */
    @Column(nullable = false)
    private String nickname;
    /** 휴대전화번호  */
    @Column(nullable = false)
    private String mobile;
    /** 프로필사진  */
    @Column(nullable = false)
    private String profileImage;
    /** 티어  */
    private String tier;
    /** 인증 */
    @Enumerated(STRING)
    private Role role;
    /** 포인트  */
    private Integer point;
    /** 연령대 */
    private String age;
    @Embedded
    private Agree agree;

    public static Member from(MemberDTO memberDTO) {
        return Member.builder()
                .token(memberDTO.getToken())
                .email(memberDTO.getEmail())
                .nickname(memberDTO.getNickname())
                .mobile(memberDTO.getMobile())
                .profileImage(memberDTO.getProfileImage())
                .age(memberDTO.getAge())
                .nickname(memberDTO.getNickname())
                .role(USER)
                .build();
    }
}
