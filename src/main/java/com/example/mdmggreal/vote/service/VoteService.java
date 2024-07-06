package com.example.mdmggreal.vote.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.ingameinfo.repository.InGameInfoQueryRepository;
import com.example.mdmggreal.ingameinfo.repository.InGameInfoRepository;
import com.example.mdmggreal.ingameinfo.type.Tier;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.post.repository.PostRepository;
import com.example.mdmggreal.vote.dto.request.VoteAddRequest;
import com.example.mdmggreal.vote.entity.Vote;
import com.example.mdmggreal.vote.repository.VoteQueryRepository;
import com.example.mdmggreal.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.mdmggreal.global.exception.ErrorCode.*;

import com.example.mdmggreal.vote.dto.request.VoteAddRequest.VoteAddDTO;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final MemberRepository memberRepository;
    private final VoteRepository voteRepository;
    private final VoteQueryRepository voteQueryRepository;
    private final PostRepository postRepository;
    private final InGameInfoQueryRepository inGameInfoQueryRepository;
    private final InGameInfoRepository inGameInfoRepository;

    @Transactional
    public void addVotes(VoteAddRequest request, Long memberId, Long postId) {
        List<VoteAddDTO> voteAddDTOList = request.getVoteList();
        Member member = getMemberById(memberId);
        List<InGameInfo> inGameInfoList = inGameInfoRepository.findByPostId(postId);

        // 클라이언트 요청 검증
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

    @Transactional(readOnly = true)
    public VoteResultResponse getVoteResult(Long postId, Long memberId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_POST));

        List<Object[]> results = voteRepository.findChampionNamesWithAverageRatioByPostId(postId);
        List<InGameInfo> inGameInfos = new ArrayList<>();
        List<InGameInfoResult> inGameInfoResults = new ArrayList<>();

        for (Object[] result : results) {
            InGameInfo inGameInfo = (InGameInfo) result[0];
            Double average = (Double) result[1];

            inGameInfos.add(inGameInfo);
            inGameInfoResults.add(
                    InGameInfoResult.builder()
                            .championName(inGameInfo.getChampionName())
                            .votedRatio(average)
                            .position(Position.fromPosition(inGameInfo.getPosition()))
                            .tier(Tier.fromTier(inGameInfo.getTier()))
                            .build()
            );
        }

        boolean hasPermission = false; // 투표 결과조회 권한 검사
        while (!hasPermission) {
            if (memberId.equals(post.getMember().getId())) hasPermission = true; // 글 작성자가 조회한 경우

            for (InGameInfo inGameInfo : inGameInfos) { // 투표 참여자가 조회한 경우
                if (voteRepository.findByMemberIdAndInGameInfoId(memberId, inGameInfo.getId()).isPresent()) {
                    hasPermission = true;
                    break;
                }
            }

            if (!hasPermission) throw new CustomException(ErrorCode.NO_PERMISSION_TO_VIEW_RESULT);
        }

        return VoteResultResponse.from(postId, inGameInfoResults, HttpStatus.OK);
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
        Tier tier = Tier.getTier(member.getJoinedResult(), member.getPredictedResult());
        if (!member.getTier().equals(tier)) {
            member.updateTier(tier);
        }
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(INVALID_USER_ID)
        );
    }

    private void rewardPoint(Member member) {
        if (member.getJoinedResult() / 3 == 0 && member.getJoinedResult() != 0) {
            member.rewardPointByJoinedResult(member.getTier().getJoinedResultPoint());
        }
    }

    private void increaseInGameInfoTotalRatio(List<InGameInfo> inGameInfoList, List<VoteAddDTO> voteAddDTOList) {
        for (InGameInfo inGameInfo : inGameInfoList) {
            int votedRatio = voteAddDTOList.stream().filter(dto -> dto.getInGameInfoId().equals(inGameInfo.getId()))
                    .findFirst().get().getRatio();
            inGameInfo.increaseTotalRatio(votedRatio);
        }
    }
}
