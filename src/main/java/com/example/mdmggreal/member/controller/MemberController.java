package com.example.mdmggreal.member.controller;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.member.dto.MemberDTO;
import com.example.mdmggreal.member.dto.response.MobileCheckResponse;
import com.example.mdmggreal.member.dto.response.NicknameCheckResponse;
import com.example.mdmggreal.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /*
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse> signup(@RequestBody MemberDTO memberDTO) {
        memberService.signup(memberDTO);
        return ResponseEntity.ok(BaseResponse.from(HttpStatus.CREATED));
    }

    @GetMapping("/mobilecheck")
    public ResponseEntity<MobileCheckResponse> mobileCheck(@RequestParam("mobile") String mobile) {
        String phone = memberService.checkMobile(mobile);
        return ResponseEntity.ok(MobileCheckResponse.from(phone, HttpStatus.OK));
    }

    /*
     * 닉네임 중복 체크
     */
    @GetMapping("/nicknamecheck")
    public ResponseEntity<NicknameCheckResponse> nicknameCheck(@RequestParam String nickname) {
        Boolean check = memberService.checkNickname(nickname);
        return ResponseEntity.ok(NicknameCheckResponse.of(HttpStatus.OK, check));
    }
}
