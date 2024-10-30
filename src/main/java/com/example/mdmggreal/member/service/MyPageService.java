package com.example.mdmggreal.member.service;

import com.example.mdmggreal.common.dto.PageInfo;
import com.example.mdmggreal.member.dto.request.DeleteProfileRequest;
import com.example.mdmggreal.member.dto.request.UpdateNickNameRequest;
import com.example.mdmggreal.member.dto.response.MemberProfileDTO;
import com.example.mdmggreal.member.dto.response.PostsByMemberGetResponse;
import com.example.mdmggreal.member.dto.response.VotedPostsByMemberGetResponse;
import com.example.mdmggreal.member.entity.Member;
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

import static com.example.mdmggreal.post.entity.type.PostStatus.FINISHED;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final PostQueryRepository postQueryRepository;
    private final S3Service s3Service;
    private final VoteResultService voteResultService;
    private final MemberGetService memberGetService;

    @Transactional(readOnly = true)
    public PostsByMemberGetResponse getPostsByMemberWithPagination(Long memberId, int pageNumber, int pageSize) {
        memberGetService.getMemberByIdOrThrow(memberId);

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<PostsByMemberGetResponse.MyPost> postsByMember = postQueryRepository.getPostsByMemberWithPagination(memberId, pageable);

        return PostsByMemberGetResponse.of(postsByMember);
    }

    @Transactional(readOnly = true)
    public MemberProfileDTO memberGet(Long memberId) {
        Member member = memberGetService.getMemberByIdOrThrow(memberId);
        return MemberProfileDTO.from(member);
    }

    @Transactional
    public void updateNickName(UpdateNickNameRequest request) {
        Member member = memberGetService.getMemberByIdOrThrow(request.getMemberId());

        // 7일에 한번만 변경할 수 있도록 확인

        member.updateNickName(request.getNickName());
    }

    @Transactional
    public void deleteProfile(DeleteProfileRequest request) {
        Member member = memberGetService.getMemberByIdOrThrow(request.getMemberId());
        member.deleteProfile();
    }

    @Transactional(readOnly = true)
    public VotedPostsByMemberGetResponse getVotedPostsByMemberPagination(Long memberId, Pageable pageable) {
        memberGetService.getMemberByIdOrThrow(memberId);

        Page<VotedPostVo> votedPostVoList = postQueryRepository.getVotedPostsByMemberIdPagination(memberId, pageable);
        List<VotedPostsByMemberGetResponse.VotedPost> responseDtoList = new ArrayList<>();

        votedPostVoList.forEach(post -> {
            // 게시글이 판결 완료된 경우에만 회원의 판결 결과 계산
            String myVoteResult = post.getStatus().equals(FINISHED) ? voteResultService.calculateMemberResultByPostId(post.getPostId(), memberId).getValue() : null;
            responseDtoList.add(VotedPostsByMemberGetResponse.VotedPost.from(post, myVoteResult));
        });

        return VotedPostsByMemberGetResponse.from(PageInfo.from(votedPostVoList), responseDtoList);
    }

    @Transactional
    public void updateProfileImage(Long memberId, MultipartFile profileImage) throws IOException {
        String imageUrl = s3Service.uploadImages(profileImage);
        Member member = memberGetService.getMemberByIdOrThrow(memberId);
        member.updateProfile(imageUrl);
    }
}
