package com.example.mdmggreal.member.controller;

import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.member.dto.MemberDTO;
import com.example.mdmggreal.member.dto.request.SignUpRequest;
import com.example.mdmggreal.member.dto.request.TokenRefreshRequest;
import com.example.mdmggreal.member.dto.response.NicknameCheckResponse;
import com.example.mdmggreal.member.dto.response.SignUpResponse;
import com.example.mdmggreal.member.dto.response.TokenRefreshResponse;
import com.example.mdmggreal.member.service.MemberService;
import com.example.mdmggreal.oauth.dto.AuthTokens;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    /*
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signup(@RequestBody @Valid SignUpRequest request) {
        return ResponseEntity.ok(memberService.signUp(request));
    }

    /*
     * 닉네임 중복 체크
     */
    @GetMapping("/nicknamecheck")
    public ResponseEntity<NicknameCheckResponse> nicknameCheck(@RequestParam("nickname") String nickname) {
        Boolean check = memberService.checkNickname(nickname);
        return ResponseEntity.ok(NicknameCheckResponse.of(HttpStatus.OK, check));
    }

    /**
     * access, refresh 토큰 재발급
     */
    @PostMapping("/token/refresh")
    public ResponseEntity<TokenRefreshResponse> tokenRefresh(@RequestBody TokenRefreshRequest request) {
        AuthTokens tokens = jwtUtil.refreshTokens(request.getRefreshToken());
        return ResponseEntity.ok(TokenRefreshResponse.of(HttpStatus.OK, tokens));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable(value = "userId") Long userId) {
        MemberDTO memberDTO = memberService.memberGet(userId);
        return ResponseEntity.ok(MemberResponse.of(HttpStatus.OK, memberDTO));
    }
}