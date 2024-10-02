package com.example.mdmggreal.member.service;

import com.example.mdmggreal.common.dto.PageInfo;
import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.ingameinfo.dto.response.InGameInfoDTO;
import com.example.mdmggreal.member.dto.response.VotedPostsByMemberGetResponse;
import com.example.mdmggreal.member.dto.response.MemberProfileDTO;
import com.example.mdmggreal.member.dto.response.PostsByMemberGetResponse;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.post.dto.vo.VotedPostVo;
import com.example.mdmggreal.post.repository.PostQueryRepository;
import com.example.mdmggreal.post.service.PostService;
import com.example.mdmggreal.vote.entity.Vote;
import com.example.mdmggreal.vote.repository.VoteQueryRepository;
import com.example.mdmggreal.vote.type.VoteResultType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.mdmggreal.global.exception.ErrorCode.INVALID_USER_ID;
import static com.example.mdmggreal.post.entity.type.PostStatus.FINISHED;
import static com.example.mdmggreal.vote.type.VoteResultType.*;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final PostQueryRepository postQueryRepository;
    private final MemberRepository memberRepository;
    private final VoteQueryRepository voteQueryRepository;

    private final PostService postService;

    @Transactional(readOnly = true)
    public PostsByMemberGetResponse getPostsByMemberWithPagination(Long memberId, int pageNumber, int pageSize) {
        getMemberByMemberId(memberId);

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<PostsByMemberGetResponse.MyPost> postsByMember = postQueryRepository.getPostsByMemberWithPagination(memberId, pageable);

        return PostsByMemberGetResponse.of(postsByMember);
    }

    /**
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

    public MemberProfileDTO memberGet(Long memberId) {
        Member member = getMemberByMemberId(memberId);
        return MemberProfileDTO.from(member);
    }

    @Transactional(readOnly = true)
    public VotedPostsByMemberGetResponse getVotedPostsByMemberPagination(Long memberId, Pageable pageable) {
        getMemberByMemberId(memberId);

        Page<VotedPostVo> postVoList = postQueryRepository.getVotedPostsByMemberIdPagination(memberId, pageable);

        List<VotedPostsByMemberGetResponse.VotedPost> responseDtoList = new ArrayList<>();
        postVoList.forEach(post -> {
            // 게시글이 판결 완료된 경우에만 회원의 판결 결과 계산
            String myVoteResult = post.getStatus().equals(FINISHED) ? calculateMyVoteResult(post, memberId).toString() : null;
            responseDtoList.add(VotedPostsByMemberGetResponse.VotedPost.from(post, myVoteResult));
        });

        return VotedPostsByMemberGetResponse.from(PageInfo.from(postVoList), responseDtoList);
    }

    /**
     * 게시글 1개에 대한 회원의 판결 결과 계산
     */
    private VoteResultType calculateMyVoteResult(VotedPostVo postVo, Long votedMemberId) {
        // 게시글의 모든 inGameInfo의 판결 결과
        List<InGameInfoDTO> inGameInfoDTOList = postService.createInGameInfoDTOList(postVo.getPostId());


        Map<Long, Double> inGameInfoMap = inGameInfoDTOList.stream()
                .collect(Collectors.toMap(InGameInfoDTO::getInGameInfoId, InGameInfoDTO::getAverageRatio));
        List<Long> maxInGameInfoList = getMaxInGameInfoList(inGameInfoMap); // 판결 결과, 최대 투표를 받은 inGameInfo id 목록

        // 모든 inGameInfo 결과가 같으면 '무승부' 반환
        if (maxInGameInfoList.size() == inGameInfoDTOList.size()) {
            return DRAW;
        }

        // 무승부가 아닌 경우 아래 로직들 실행

        // 게시글에 대한 회원의 투표 정보
        List<Vote> voteList = voteQueryRepository.getVoteListByMemberIdAndPostId(votedMemberId, postVo.getPostId());
        Map<Long, Double> memberInGameInfoMap = voteList.stream()
                .collect(Collectors.toMap(vote -> vote.getInGameInfo().getId(), vote -> vote.getRatio().doubleValue()));
        List<Long> maxMemberVotedInGameInfoList = getMaxInGameInfoList(memberInGameInfoMap); // 회원이 최대 비율로 투표한 inGameInfo id 목록

        // 회원의 최종 판결 결과
        // 회원의 최대 판결 inGameInfo 목록과 게시글의 최대 판결 inGameInfo 중 한개라도 일치하면 승리
        return maxInGameInfoList.stream().anyMatch(maxMemberVotedInGameInfoList::contains) ? VICTORY : DEFEAT;
    }

    /**
     * 최대 판결 inGameInfo id의 목록
     */
    private List<Long> getMaxInGameInfoList(Map<Long, Double> inGameInfoMap) {
        List<Long> maxInGameInfoList = new ArrayList<>();
        final Double[] maxAverageRatio = {0.0}; // 최대 판결 비율

        inGameInfoMap.forEach((inGameInfoId, ratio) -> {
            if (maxAverageRatio[0] < ratio) {
                maxAverageRatio[0] = ratio;
                maxInGameInfoList.clear();
                maxInGameInfoList.add(inGameInfoId);
            } else if (maxAverageRatio[0].equals(ratio)) {
                maxInGameInfoList.add(inGameInfoId);
            }
        });

        return maxInGameInfoList;
    }

}
