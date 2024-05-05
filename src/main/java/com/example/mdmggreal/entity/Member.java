package com.example.mdmggreal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "member")
@Getter @Setter
public class Member {
    @Id
    private String memberId;

    /** 이름  */
    private String name;
    /** 이메일  */
    private String email;
    /** 닉네임  */
    private String nickname;
    /** 성별  */
    private String gender;
    /** 전화번호  */
    private String mobileNumber;
    /** 나이  */
    private Integer age;
    /** 프로필사진  */
    private String profileImage;
    /** 인증  */
    private String authentication;
    /** 티어  */
    private String tier;
    /** 포인트  */
    private Integer point;
    /** 생성일자  */
    @Temporal(value= TemporalType.TIMESTAMP)
    private Date createDateTime;
    /** 수정일자  */
    @Temporal(value= TemporalType.TIMESTAMP)
    private Date modifyDateTime;
}
