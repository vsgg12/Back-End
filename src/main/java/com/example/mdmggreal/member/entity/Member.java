package com.example.mdmggreal.member.entity;

import com.example.mdmggreal.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "member")
@Getter @Setter
@SuperBuilder
public class Member extends BaseEntity {
    public Member() {}
    @Id
    // 증가 추가..
    private Long id;

    // tokenㅇ로 바꾸기....
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
    /** 티어  */
    private String tier;
    /** 포인트  */
    private Integer point;

    public Member(String nickname, String email, String mobile, String memberId, String profileImage) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.email = email;
        this.mobile = mobile;
        this.profileImage = profileImage;
    }
}
