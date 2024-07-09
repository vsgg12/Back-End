package com.example.mdmggreal.vote.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.ingameinfo.repository.InGameInfoRepository;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.member.type.MemberTier;
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

    private final MemberRepository memberRepository;
    private final VoteRepository voteRepository;
    private final VoteQueryRepository voteQueryRepository;
    private final InGameInfoRepository inGameInfoRepository;

    @Transactional
    public void addVotes(VoteAddRequest request, Long memberId, Long postId) {
        List<VoteAddDTO> voteAddDTOList = request.getVoteList();
        Member member = getMemberById(memberId);
        List<InGameInfo> inGameInfoList = inGameInfoRepository.findByPostId(postId);

        // 클라이언트 요청 검증
        validateIsPostAuthor(inGameInfoList, memberId);
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

    private void validateIsPostAuthor(List<InGameInfo> inGameInfoList, Long memberId) {
        if (memberId.equals(inGameInfoList.get(0).getPost().getMember().getId()))
            throw new CustomException(INVALID_VOTE_OF_AUTHOR);
    }

    private void validateVoteExistence(Long postId, Member member) {
        if (voteQueryRepository.existsVoteByMemberId(postId, member.getId())) {
            throw new CustomException(VOTE_ALREADY_EXISTS);
        }
    }

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

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(INVALID_USER_ID)
        );
    }

    private void rewardPoint(Member member) {
        if (member.getJoinedResult() / 3 == 0 && member.getJoinedResult() != 0) {
            member.rewardPointByJoinedResult(member.getMemberTier().getJoinedResultPoint());
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
