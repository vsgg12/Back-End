package com.example.mdmggreal.member.controller;

import com.example.mdmggreal.member.dto.MemberDTO;
import com.example.mdmggreal.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.example.mdmggreal.global.exception.ErrorCode.NICKNAME_ALREADY_EXISTS;

@RestController
@RequestMapping("/api/users")
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
    public ResponseEntity<?> callback(HttpServletRequest request) throws Exception {
        MemberDTO memberDTO = memberService.getNaverInfo(request.getParameter("code"));

        String isMemberYn = "N";

        if(memberService.isMemberExist(memberDTO.getToken())) {
            isMemberYn = "Y";
        }

        Map<String, Object> response = new HashMap<>();
        response.put("member", memberDTO);
        response.put("isMember", isMemberYn);

        return ResponseEntity.ok()
                .body(response);
    }

}
