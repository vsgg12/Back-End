package com.example.mdmggreal.member.entity;

import com.example.mdmggreal.global.entity.BaseEntity;
import com.example.mdmggreal.ingameinfo.type.Tier;
import com.example.mdmggreal.member.dto.request.SignUpRequest;
import com.example.mdmggreal.member.type.Agree;
import com.example.mdmggreal.member.type.OAuthProvider;
import com.example.mdmggreal.member.type.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import static com.example.mdmggreal.ingameinfo.type.Tier.UNRANK;
import static com.example.mdmggreal.member.type.OAuthProvider.NAVER;
import static com.example.mdmggreal.member.type.Role.USER;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    /**
     * 이메일
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * 닉네임
     */
    @Column(nullable = false, unique = true)
    private String nickname;

    /**
     * 휴대전화번호
     * 일단 null 로 두고, 추후 전화번호 인증 추가되면 로직추가하기
     */
    private String mobile;

    /**
     * 프로필사진
     */
    @Column(nullable = false)
    private String profileImage;

    /**
     * 티어
     */
    @Enumerated(STRING)
    @Column(nullable = false)
    private Tier tier;

    /**
     * 인증
     */
    @Enumerated(STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * 포인트
     */
    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer point;

    /**
     * 약관 동의 여부
     */
    @Embedded
    @Column(nullable = false)
    private Agree agree;

    /**
     * 맞춘 판결 수
     */
    @ColumnDefault("0")
    private Integer predictedResult;

    /**
     * 참여한 판결 수
     */
    @ColumnDefault("0")
    private Integer joinedResult;

    /**
     * SNS 가입 경로
     */
    @Enumerated(STRING)
    @Column(nullable = false)
    private OAuthProvider oAuthProvider;

    /**
     * 네이버 로그인
     */
    public static Member from(SignUpRequest request) {
        return Member.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .mobile(null)
                .profileImage(request.getProfileImage())
                .agree(Agree.builder()
                        .agreeAge(request.getAgrees().isAgreeAge())
                        .agreeTerms(request.getAgrees().isAgreeTerms())
                        .agreePrivacy(request.getAgrees().isAgreePrivacy())
                        .agreePromotion(request.getAgrees().isAgreePromotion())
                        .build())
                .role(USER)
                .tier(UNRANK)
                .oAuthProvider(NAVER)
                .predictedResult(0)
                .joinedResult(0)
                .point(0)
                .build();
    }

    public void editPredictedResult() {
        this.predictedResult += 1;
    }

    public void editJoinedResult() {
        this.joinedResult += 1;
    }

    public void updateTier(Tier tier) {
        this.tier = tier;
    }

    public void rewardPointByComment(int point) {
        this.point += point;
    }

    public void rewardPointByJoinedResult(Integer point) {
        this.point += point;
    }

    public void rewardPointByPostCreation(Integer point) {
        this.point += point;
    }
}
