package com.example.mdmggreal.member;

import com.example.mdmggreal.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "member")
@Getter @Setter
public class Member extends BaseEntity {
    @Id
    private String memberId;

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
    /** 인증  */
    private String authentication;
    /** 티어  */
    private String tier;
    /** 포인트  */
    private Integer point;

    @Enumerated(EnumType.STRING)
    private MemberRole role;
}
