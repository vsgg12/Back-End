package com.example.mdmggreal.member.controller;

import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.member.dto.response.MemberProfileDTO;
import com.example.mdmggreal.member.dto.response.PostsByMemberGetResponse;
import com.example.mdmggreal.member.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    /**
     * 회원이 작성한 글 목록 조회 - 페이지네이션
     * 페이지 시작 번호 : 1
     */
    @GetMapping("/post")
    public ResponseEntity<PostsByMemberGetResponse> postsByMemberGet(
            @RequestHeader(value = "Authorization") String token,
            @PageableDefault(size = 5, page = 1) Pageable pageable
    ) {
        Long memberId = JwtUtil.getMemberId(token);
        return ResponseEntity.ok(
                myPageService.getPostsByMemberWithPagination(memberId, pageable.getPageNumber(), pageable.getPageSize())
        );
    }

    @GetMapping
    public ResponseEntity<MemberResponse> getMember(@RequestHeader(value = "Authorization") String token) {
        Long memberId = JwtUtil.getMemberId(token);
        MemberProfileDTO memberProfileDTO = myPageService.memberGet(memberId);
        return ResponseEntity.ok(MemberResponse.of(HttpStatus.OK, memberProfileDTO));
    }

}
