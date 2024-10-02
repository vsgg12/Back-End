package com.example.mdmggreal.member.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.member.dto.request.DeleteProfileRequest;
import com.example.mdmggreal.member.dto.request.UpdateNickNameRequest;
import com.example.mdmggreal.member.dto.request.UpdateProfileImageRequest;
import com.example.mdmggreal.member.dto.response.MemberProfileDTO;
import com.example.mdmggreal.member.dto.response.PostsByMemberGetResponse;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.post.repository.PostQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.mdmggreal.global.exception.ErrorCode.INVALID_USER_ID;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final PostQueryRepository postQueryRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public PostsByMemberGetResponse getPostsByMember(Long memberId) {
        Member loginMember = getMemberByMemberId(memberId);
        List<PostsByMemberGetResponse.MyPost> posts = postQueryRepository.getPostsMember(loginMember.getId());

        return PostsByMemberGetResponse.builder()
                .resultCode(HttpStatus.OK.value())
                .resultMsg(HttpStatus.OK.name())
                .postList(posts)
                .build();
    }

    @Transactional(readOnly = true)
    public MemberProfileDTO memberGet(Long memberId) {
        Member member = getMemberByMemberId(memberId);
        return MemberProfileDTO.from(member);
    }

    @Transactional
    public void updateProfileImage(UpdateProfileImageRequest request) {
        Member member = getMemberByMemberId(request.getMemberId());
        member.updateProfile(request.getProfileUrl());
    }

    @Transactional
    public void updateNickName(UpdateNickNameRequest request) {
        Member member = getMemberByMemberId(request.getMemberId());
        member.updateNickName(request.getNickName());
    }

    @Transactional
    public void deleteProfile(DeleteProfileRequest request) {
        Member member = getMemberByMemberId(request.getMemberId());
        member.deleteProfile();
    }

    private Member getMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(INVALID_USER_ID)
        );
    }
}
