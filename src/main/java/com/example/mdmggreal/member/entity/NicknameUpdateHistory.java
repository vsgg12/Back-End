package com.example.mdmggreal.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class NicknameUpdateHistory {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Comment("닉네임 변경 회원 ID")
    @Column(nullable = false)
    private Long memberId;

    @Comment("변경 전 닉네임")
    @Column(nullable = false)
    private String oldNickname;

    @Comment("변경 후 닉네임")
    @Column(nullable = false)
    private String newNickname;

    @Comment("닉네임 변경 일시")
    @Column(nullable = false)
    private LocalDateTime createdDateTime;
}
