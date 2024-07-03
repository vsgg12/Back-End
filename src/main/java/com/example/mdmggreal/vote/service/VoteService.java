package com.example.mdmggreal.vote.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.ingameinfo.repository.InGameInfoQueryRepository;
import com.example.mdmggreal.ingameinfo.repository.InGameInfoRepository;
import com.example.mdmggreal.ingameinfo.type.Position;
import com.example.mdmggreal.ingameinfo.type.Tier;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.post.repository.PostRepository;
import com.example.mdmggreal.vote.dto.VoteResultResponse;
import com.example.mdmggreal.vote.dto.VoteSaveDTO;
import com.example.mdmggreal.vote.entity.Vote;
import com.example.mdmggreal.vote.repository.VoteQueryRepository;
import com.example.mdmggreal.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.mdmggreal.global.exception.ErrorCode.*;
import static com.example.mdmggreal.vote.dto.VoteResultResponse.InGameInfoResult;

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
    public void saveVotes(List<VoteSaveDTO> voteSaveDTOS, Long memberId, Long postId) {
        Member member = getMemberById(memberId);
        List<InGameInfo> inGameInfoList = inGameInfoRepository.findByPostId(postId);

        validateVoteExistence(postId, member);
        validateInGameInfoId(postId, inGameInfoList, voteSaveDTOS);
        validateVotesTotalValue(voteSaveDTOS);

        updateMemberAfterVote(member);
        List<Vote> votes = convertToVoteEntities(voteSaveDTOS, memberId);

        for (InGameInfo inGameInfo : inGameInfoList) {
            Double averageRatioByPostId = inGameInfoQueryRepository.getAverageRatioByPostId(inGameInfo.getId());
            inGameInfo.updateAverageRatio(averageRatioByPostId);
        }

        voteRepository.saveAll(votes);
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

    private void validateInGameInfoId(Long postId, List<InGameInfo> inGameInfoList, List<VoteSaveDTO> voteSaveDTOS) {
        for (VoteSaveDTO voteSaveDTO : voteSaveDTOS) {
            boolean exists = inGameInfoRepository.existsByIdAndPostId(voteSaveDTO.getIngameInfoId(), postId);
            if (!exists) {
                throw new CustomException(NOT_MATCH_IN_GAME_INFO);
            }
        }

        if (inGameInfoList.size() != voteSaveDTOS.size()) {
            throw new CustomException(ALL_IN_GAME_INFO_VOTE_REQUIRED);
        }
    }

    private void validateVotesTotalValue(List<VoteSaveDTO> voteSaveDTOS) {
        long sum = voteSaveDTOS.stream()
                .mapToLong(VoteSaveDTO::getRatio)
                .sum();
        if (sum != 10) throw new CustomException(VOTES_TOTAL_VALUE_MUST_BE_TEN);
    }

    private void updateMemberAfterVote(Member member) {
        member.editJoinedResult();
        Tier tier = Tier.getTier(member.getJoinedResult(), member.getPredictedResult());
        member.updateTier(tier);
    }

    private List<Vote> convertToVoteEntities(List<VoteSaveDTO> voteSaveDTOS, Long memberId) {
        List<Vote> voteList = new ArrayList<>();
        for (VoteSaveDTO voteSaveDTO : voteSaveDTOS) {
            voteList.add(convertToEntity(voteSaveDTO, memberId));
        }

        return voteList;
    }

    private Vote convertToEntity(VoteSaveDTO voteSaveDTO, Long memberId) {
        Member member = getMemberById(memberId);
        InGameInfo inGameInfo = new InGameInfo(voteSaveDTO.getIngameInfoId());
        return Vote.builder()
                .ratio(voteSaveDTO.getRatio())
                .memberId(member.getId())
                .inGameInfo(inGameInfo)
                .build();
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(INVALID_USER_ID)
        );
    }
}
