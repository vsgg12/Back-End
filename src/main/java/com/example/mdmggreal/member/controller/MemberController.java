package com.example.mdmggreal.member.controller;

import com.example.mdmggreal.member.dto.MemberDTO;
import com.example.mdmggreal.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.example.mdmggreal.global.exception.ErrorCode.NICKNAME_ALREADY_EXISTS;

@RestController
@RequestMapping("/api/users")
@SessionAttributes("token") // 세션에 memberDTO 속성을 추가
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /*
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody MemberDTO memberDTO) {
        try {
            memberService.signup(memberDTO);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원가입에 실패했습니다. " + e.getMessage());
        }
    }

    /*
     * 닉네임 중복 체크
     */
    @GetMapping("/check")
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        if (memberService.isNicknameAvailable(nickname)) {
            return ResponseEntity.ok("닉네임을 사용할 수 있습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(NICKNAME_ALREADY_EXISTS);
        }
    }

    /*
     * 네이버 로그인 정보 콜백
     */
    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam String code, @RequestBody String state, HttpServletResponse response) throws Exception {
        MemberDTO memberDTO = memberService.getNaverInfo(code);

        String isMemberYn = "N";
        String isMobileYn = "N";

        if (memberService.isTokenExist(memberDTO.getToken())) {
            isMemberYn = "Y";
        }

        if (memberService.isMobileExist(memberDTO.getMobile())) {
            isMobileYn = "Y";
        }

        // 사용자 정보를 JSON 문자열로 변환하여 쿠키에 저장
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("token", memberDTO.getToken());
        userInfo.put("isMemberYn", isMemberYn);
        userInfo.put("isMobileYn", isMobileYn);
        userInfo.put("nickname", memberDTO.getNickname());
        userInfo.put("email", memberDTO.getEmail());
        userInfo.put("profileImage", memberDTO.getProfileImage());
        userInfo.put("mobile", memberDTO.getMobile());
        userInfo.put("gender", memberDTO.getGender());
        userInfo.put("age", memberDTO.getAge());

        String userInfoJson = URLEncoder.encode(new ObjectMapper().writeValueAsString(userInfo), StandardCharsets.UTF_8.toString());
        Cookie userInfoCookie = new Cookie("userInfo", userInfoJson);

        // 쿠키 설정
        int maxAge = 60 * 5; // 5분 동안 유지
        userInfoCookie.setMaxAge(maxAge);
        userInfoCookie.setPath("/"); // 모든 경로에서 쿠키 접근 가능
        userInfoCookie.setHttpOnly(true); // JavaScript에서 쿠키 접근 불가능하게 설정
        response.addCookie(userInfoCookie);

        // 프론트엔드로 리다이렉트
        response.sendRedirect("http://localhost:3000/api/oauth/naver/callback");

        return ResponseEntity.ok(response);
    }

    /*
     * 로그아웃 구현
     */
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // 세션 삭제
        return ResponseEntity.ok("로그아웃되었습니다.");
    }

}
