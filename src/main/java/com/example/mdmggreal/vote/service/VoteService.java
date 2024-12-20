package com.example.mdmggreal.vote.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.ingameinfo.repository.InGameInfoRepository;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.service.MemberGetService;
import com.example.mdmggreal.member.type.MemberTier;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.post.entity.type.PostStatus;
import com.example.mdmggreal.post.repository.PostRepository;
import com.example.mdmggreal.vote.dto.request.VoteAddRequest;
import com.example.mdmggreal.vote.dto.request.VoteAddRequest.VoteAddDTO;
import com.example.mdmggreal.vote.entity.Vote;
import com.example.mdmggreal.vote.repository.VoteQueryRepository;
import com.example.mdmggreal.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.mdmggreal.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final VoteQueryRepository voteQueryRepository;
    private final InGameInfoRepository inGameInfoRepository;
    private final PostRepository postRepository;
    private final MemberGetService memberGetService;

    @Transactional
    public void addVotes(VoteAddRequest request, Long memberId, Long postId) {
        List<VoteAddDTO> voteAddDTOList = request.getVoteList();
        Member member = memberGetService.getMemberByIdOrThrow(memberId);
        List<InGameInfo> inGameInfoList = inGameInfoRepository.findByPostId(postId);

        // 클라이언트 요청 검증
        validateSelfVoting(postId, memberId);
        validateIsProgressPost(postId);
        validateVoteExistence(postId, member);
        validateInGameInfoId(inGameInfoList, voteAddDTOList);
        validateVotesTotalValue(voteAddDTOList);

        // InGameInfo 투표 총합 업데이트
        increaseInGameInfoTotalRatio(inGameInfoList, voteAddDTOList);

        // 투표 저장
        List<Vote> savedVotes = convertToVoteEntities(voteAddDTOList, inGameInfoList, member);
        voteRepository.saveAll(savedVotes);

        // Member 업데이트
        updateMemberAfterVote(member);
        rewardPoint(member);
    }

    /*
    본인의 게시글에 투표 못하도록 검증
     */
    private void validateSelfVoting(Long postId, Long memberId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(INVALID_POST));
        if (memberId.equals(post.getMember().getId())) {
            throw new CustomException(ErrorCode.CANNOT_VOTE_OWN_POST);
        }
    }

    /*
    판결 중인 게시글인지 검증
     */
    private void validateIsProgressPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(INVALID_POST));
        if (!PostStatus.PROGRESS.equals(post.getStatus())) {
            throw new CustomException(ErrorCode.CANNOT_VOTE_TO_FINISHED_POST);
        }
    }

    /*
    이미 투표한 게시글인지 검증
     */
    private void validateVoteExistence(Long postId, Member member) {
        if (voteQueryRepository.existsVoteByMemberId(postId, member.getId())) {
            throw new CustomException(VOTE_ALREADY_EXISTS);
        }
    }

    /*
    게시글에 해당하는 inGameInfo 를 전부 요청했는지 검증
     */
    private void validateInGameInfoId(List<InGameInfo> inGameInfoList, List<VoteAddDTO> voteAddDTOList) {
        for (VoteAddDTO voteAddDTO : voteAddDTOList) {
            boolean isMatchedInGameInfo = inGameInfoList.stream()
                    .anyMatch(inGameInfo -> inGameInfo.getId().equals(voteAddDTO.getInGameInfoId()));
            if (!isMatchedInGameInfo) {
                throw new CustomException(ErrorCode.NOT_MATCH_IN_GAME_INFO);
            }
        }

        if (inGameInfoList.size() != voteAddDTOList.size()) {
            throw new CustomException(ALL_IN_GAME_INFO_VOTE_REQUIRED);
        }
    }

    /*
    투표의 총합이 10인지 검증
     */
    private void validateVotesTotalValue(List<VoteAddDTO> VoteAddDTOs) {
        long sum = VoteAddDTOs.stream().mapToLong(VoteAddDTO::getRatio).sum();
        if (sum != 10) throw new CustomException(VOTES_TOTAL_VALUE_MUST_BE_TEN);
    }

    private List<Vote> convertToVoteEntities(List<VoteAddDTO> voteAddDTOList,
                                             List<InGameInfo> inGameInfoList,
                                             Member member) {
        List<Vote> savedVotes = new ArrayList<>();
        for (VoteAddDTO voteAddDTO : voteAddDTOList) {
            Vote savedVote = Vote.builder()
                    .memberId(member.getId())
                    .inGameInfo(inGameInfoList.stream()
                            .filter(inGameInfo -> inGameInfo.getId().equals(voteAddDTO.getInGameInfoId())).findFirst().get())
                    .ratio(voteAddDTO.getRatio())
                    .build();
            savedVotes.add(savedVote);
        }
        return savedVotes;
    }

    private void updateMemberAfterVote(Member member) {
        member.editJoinedResult();
        MemberTier memberTier = MemberTier.getTier(member.getJoinedResult(), member.getPredictedResult());
        if (!member.getMemberTier().equals(memberTier)) {
            member.updateTier(memberTier);
        }
    }

    private void rewardPoint(Member member) {
        if (member.getJoinedResult() / 3 == 0 && member.getJoinedResult() != 0) {
            member.increasePoint(member.getMemberTier().getJoinedResultPoint());
        }
    }

    private void increaseInGameInfoTotalRatio(List<InGameInfo> inGameInfoList, List<VoteAddDTO> voteAddDTOList) {
        for (InGameInfo inGameInfo : inGameInfoList) {
            int votedRatio = voteAddDTOList.stream().filter(dto -> dto.getInGameInfoId().equals(inGameInfo.getId()))
                    .findFirst().get().getRatio();
            inGameInfo.addTotalRatio(votedRatio);
        }
    }
}
