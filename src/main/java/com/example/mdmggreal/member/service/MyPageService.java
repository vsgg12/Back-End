package com.example.mdmggreal.member.service;

import com.example.mdmggreal.common.dto.PageInfo;
import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.member.dto.request.DeleteProfileRequest;
import com.example.mdmggreal.member.dto.request.UpdateNickNameRequest;
import com.example.mdmggreal.member.dto.response.MemberProfileDTO;
import com.example.mdmggreal.member.dto.response.PostsByMemberGetResponse;
import com.example.mdmggreal.member.dto.response.VotedPostsByMemberGetResponse;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.post.dto.vo.VotedPostVo;
import com.example.mdmggreal.post.repository.PostQueryRepository;
import com.example.mdmggreal.s3.service.S3Service;
import com.example.mdmggreal.vote.service.VoteResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.mdmggreal.global.exception.ErrorCode.INVALID_USER_ID;
import static com.example.mdmggreal.post.entity.type.PostStatus.FINISHED;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final PostQueryRepository postQueryRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final VoteResultService voteResultService;

    @Transactional(readOnly = true)
    public PostsByMemberGetResponse getPostsByMemberWithPagination(Long memberId, int pageNumber, int pageSize) {
        getMemberByMemberId(memberId);

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<PostsByMemberGetResponse.MyPost> postsByMember = postQueryRepository.getPostsByMemberWithPagination(memberId, pageable);

        return PostsByMemberGetResponse.of(postsByMember);
    }

    @Transactional(readOnly = true)
    public MemberProfileDTO memberGet(Long memberId) {
        Member member = getMemberByMemberId(memberId);
        return MemberProfileDTO.from(member);
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

    @Transactional(readOnly = true)
    public VotedPostsByMemberGetResponse getVotedPostsByMemberPagination(Long memberId, Pageable pageable) {
        getMemberByMemberId(memberId);

        Page<VotedPostVo> votedPostVoList = postQueryRepository.getVotedPostsByMemberIdPagination(memberId, pageable);
        List<VotedPostsByMemberGetResponse.VotedPost> responseDtoList = new ArrayList<>();

        votedPostVoList.forEach(post -> {
            // 게시글이 판결 완료된 경우에만 회원의 판결 결과 계산
            String myVoteResult = post.getStatus().equals(FINISHED) ? voteResultService.calculateMemberResultByPostId(post.getPostId(), memberId).getValue() : null;
            responseDtoList.add(VotedPostsByMemberGetResponse.VotedPost.from(post, myVoteResult));
        });

        return VotedPostsByMemberGetResponse.from(PageInfo.from(votedPostVoList), responseDtoList);
    }

    /*
     * 회원 조회.
     *
     * @param memberId 회원 id
     * @return Member entity
     */
    private Member getMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(INVALID_USER_ID)
        );
    }

    @Transactional
    public void updateProfileImage(Long memberId, MultipartFile profileImage) throws IOException {
        String imageUrl = s3Service.uploadImages(profileImage);
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(INVALID_USER_ID)
        );
        member.updateProfile(imageUrl);
    }
}
