package com.example.mdmggreal.member.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.member.dto.response.MemberProfileDTO;
import com.example.mdmggreal.member.dto.response.PostsByMemberGetResponse;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.post.repository.PostQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public PostsByMemberGetResponse getPostsByMemberWithPagination(Long memberId, int pageNumber, int pageSize) {
        getMemberByMemberId(memberId);

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<PostsByMemberGetResponse.MyPost> postsByMember = postQueryRepository.getPostsByMemberWithPagination(memberId, pageable);

        return PostsByMemberGetResponse.of(postsByMember);
    }

    /**
     * 회원 조회.
     * @param memberId 회원 id
     * @return Member entity
     */
    private Member getMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(INVALID_USER_ID)
        );
    }

    public MemberProfileDTO memberGet(Long memberId) {
        Member member = getMemberByMemberId(memberId);
        return MemberProfileDTO.from(member);
    }
}
