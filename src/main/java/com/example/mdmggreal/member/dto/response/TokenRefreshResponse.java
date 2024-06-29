package com.example.mdmggreal.member.dto.response;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.oauth.dto.AuthTokens;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@SuperBuilder
public class TokenRefreshResponse extends BaseResponse {
    private AuthTokens tokens;

    public static TokenRefreshResponse of(HttpStatus status, AuthTokens tokens) {
        return TokenRefreshResponse.builder()
                .resultCode(status.value())
                .resultMsg(status.name())
                .tokens(tokens)
                .build();
    }
}
