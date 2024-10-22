package com.example.mdmggreal.member.controller;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.member.dto.request.DeleteProfileRequest;
import com.example.mdmggreal.member.dto.request.UpdateNickNameRequest;
import com.example.mdmggreal.member.dto.response.MemberProfileDTO;
import com.example.mdmggreal.member.dto.response.PostsByMemberGetResponse;
import com.example.mdmggreal.member.dto.response.VotedPostsByMemberGetResponse;
import com.example.mdmggreal.member.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public ResponseEntity<MemberResponse> memberGet(@RequestHeader(value = "Authorization") String token) {
        Long memberId = JwtUtil.getMemberId(token);
        MemberProfileDTO memberProfileDTO = myPageService.memberGet(memberId);
        return ResponseEntity.ok(MemberResponse.of(HttpStatus.OK, memberProfileDTO));
    }

    @PatchMapping("/profile")
    public ResponseEntity<BaseResponse> profileImageUpdate(@RequestHeader(value = "Authorization") String token,
                                                           @RequestPart(value = "profile") MultipartFile profileImage) throws IOException {
        Long memberId = JwtUtil.getMemberId(token);
        myPageService.updateProfileImage(memberId, profileImage);
        return BaseResponse.toResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/nickname")
    public ResponseEntity<BaseResponse> nickNameUpdate(@RequestHeader(value = "Authorization") String token,
                                               @RequestBody UpdateNickNameRequest request) {
        Long memberId = JwtUtil.getMemberId(token);
        request.setMemberId(memberId);
        myPageService.updateNickName(request);
        return BaseResponse.toResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<BaseResponse> profileImageDelete(@RequestHeader(value = "Authorization") String token) {
        Long memberId = JwtUtil.getMemberId(token);
        myPageService.deleteProfile(new DeleteProfileRequest(memberId));
        return BaseResponse.toResponseEntity(HttpStatus.OK);
    }

    /**
     * 회원이 투표에 참여한 글 목록 조회 페이지네이션
     * 페이지 시작 번호 : 1
     */
    @GetMapping("/vote")
    public ResponseEntity<VotedPostsByMemberGetResponse> votedPostsByMemberGet(
            @RequestHeader(value = "Authorization") String token,
            @PageableDefault(size = 5, page = 1) Pageable pageable
    ) {
        Long memberId = JwtUtil.getMemberId(token);

        Pageable newPageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());

        return ResponseEntity.ok(
                myPageService.getVotedPostsByMemberPagination(memberId, newPageable)
        );
    }
}
