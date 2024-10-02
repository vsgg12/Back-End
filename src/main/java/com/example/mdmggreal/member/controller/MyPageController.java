package com.example.mdmggreal.member.controller;

import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.member.dto.request.DeleteProfileRequest;
import com.example.mdmggreal.member.dto.request.UpdateNickNameRequest;
import com.example.mdmggreal.member.dto.request.UpdateProfileImageRequest;
import com.example.mdmggreal.member.dto.response.MemberProfileDTO;
import com.example.mdmggreal.member.dto.response.PostsByMemberGetResponse;
import com.example.mdmggreal.member.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<MemberResponse> memberGet(@RequestHeader(value = "Authorization") String token) {
        Long memberId = JwtUtil.getMemberId(token);
        MemberProfileDTO memberProfileDTO = myPageService.memberGet(memberId);
        return ResponseEntity.ok(MemberResponse.of(HttpStatus.OK, memberProfileDTO));
    }

    @PatchMapping("/profile")
    public ResponseEntity<Void> profileImageUpdate(@RequestHeader(value = "Authorization") String token,
                                                   @RequestBody UpdateProfileImageRequest request) {
        Long memberId = JwtUtil.getMemberId(token);
        request.setMemberId(memberId);
        myPageService.updateProfileImage(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/nickname")
    public ResponseEntity<Void> nickNameUpdate(@RequestHeader(value = "Authorization") String token,
                                               @RequestBody UpdateNickNameRequest request) {
        Long memberId = JwtUtil.getMemberId(token);
        request.setMemberId(memberId);
        myPageService.updateNickName(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> profileImageDelete(@RequestHeader(value = "Authorization") String token) {
        Long memberId = JwtUtil.getMemberId(token);
        myPageService.deleteProfile(new DeleteProfileRequest(memberId));
        return ResponseEntity.ok().build();
    }
}
