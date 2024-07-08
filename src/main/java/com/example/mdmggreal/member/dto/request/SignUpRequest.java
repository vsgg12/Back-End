package com.example.mdmggreal.member.dto.request;

import lombok.Getter;

@Getter
public class SignUpRequest {

    private String email;

    private String nickname;

    private String profileImage;

    // 약관 동의 여부
    private AgreeDTO agrees;

    @Getter
    public static class AgreeDTO {
        private boolean agreeAge;

        private boolean agreeTerms;

        private boolean agreePrivacy;

        private boolean agreePromotion;
    }
}
