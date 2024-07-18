package com.example.mdmggreal.member.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SignUpRequest {

    @NotNull
    private String email;

    @NotNull
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
