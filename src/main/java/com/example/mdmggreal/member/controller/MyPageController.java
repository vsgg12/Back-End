package com.example.mdmggreal.member.controller;

import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.member.dto.response.MemberProfileDTO;
import com.example.mdmggreal.member.dto.response.PostsByMemberGetResponse;
import com.example.mdmggreal.member.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    // TODO member 패키지에 위치시킬지, post 패키지에 위치시킬지 확정 후 주석 삭제
    @GetMapping("/post")
    public ResponseEntity<PostsByMemberGetResponse> postsByMemberGet(@RequestHeader(value = "Authorization") String token) {
        Long memberId = JwtUtil.getMemberId(token);
        return ResponseEntity.ok(myPageService.getPostsByMember(memberId));
    }

    @GetMapping
    public ResponseEntity<MemberResponse> getMember(@RequestHeader(value = "Authorization") String token) {
        Long memberId = JwtUtil.getMemberId(token);
        MemberProfileDTO memberProfileDTO = myPageService.memberGet(memberId);
        return ResponseEntity.ok(MemberResponse.of(HttpStatus.OK, memberProfileDTO));
    }
}
