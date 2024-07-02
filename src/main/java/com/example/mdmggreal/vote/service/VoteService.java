package com.example.mdmggreal.vote.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.ingameinfo.repository.InGameInfoQueryRepository;
import com.example.mdmggreal.ingameinfo.repository.InGameInfoRepository;
import com.example.mdmggreal.ingameinfo.type.Tier;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.vote.dto.VoteAvgDTO;
import com.example.mdmggreal.vote.dto.VoteSaveDTO;
import com.example.mdmggreal.vote.entity.Vote;
import com.example.mdmggreal.vote.repository.VoteQueryRepository;
import com.example.mdmggreal.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.mdmggreal.global.exception.ErrorCode.INVALID_USER_ID;
import static com.example.mdmggreal.global.exception.ErrorCode.VOTE_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final MemberRepository memberRepository;
    private final VoteRepository voteRepository;
    private final VoteQueryRepository voteQueryRepository;
    private final InGameInfoQueryRepository inGameInfoQueryRepository;
    private final InGameInfoRepository inGameInfoRepository;

    public List<Vote> saveVotes(List<VoteSaveDTO> voteSaveDTOS, Long memberId, Long postId) {
        Member member = getMemberById(memberId);
        validateVoteExistence(postId, member);
        validateInGameInfoId(postId, voteSaveDTOS);


        updateMemberAfterVote(member);
        List<Vote> votes = convertToVoteEntities(voteSaveDTOS, memberId);


        List<InGameInfo> inGameInfoList = inGameInfoRepository.findByPostId(postId);
        for (InGameInfo inGameInfo : inGameInfoList) {
            Double averageRatioByPostId = inGameInfoQueryRepository.getAverageRatioByPostId(inGameInfo.getId());
            inGameInfo.updateAverageRatio(averageRatioByPostId);
        }

        return voteRepository.saveAll(votes);
    }

        private void validateInGameInfoId(Long postId, List<VoteSaveDTO> voteSaveDTOS) {
            for (VoteSaveDTO voteSaveDTO : voteSaveDTOS) {
                boolean exists = inGameInfoRepository.existsByIdAndPostId(voteSaveDTO.getIngameInfoId(), postId);
                if (!exists) {
                    throw new CustomException(ErrorCode.NOT_MATCH_IN_GAME_INFO);
                }
            }
        }

    public List<VoteAvgDTO> getChampionNamesWithAverageRatioByPostId(Long postId) {
        List<Object[]> results = voteRepository.findChampionNamesWithAverageRatioByPostId(postId);
        return results.stream()
                .map(this::convertToVoteAvgDTO)
                .collect(Collectors.toList());
    }

    public List<Post> getVotedPostsByMemberId(Long memberId) {
        Member member = getMemberById(memberId);
        return voteRepository.findByMemberId(member.getId()).stream()
                .map(Vote::getInGameInfo)
                .filter(Objects::nonNull)
                .map(InGameInfo::getPost)
                .collect(Collectors.toList());
    }

    private void validateVoteExistence(Long postId, Member member) {
        if (voteQueryRepository.existsVoteByMemberId(postId, member.getId())) {
            throw new CustomException(VOTE_ALREADY_EXISTS);
        }
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

    private VoteAvgDTO convertToVoteAvgDTO(Object[] result) {
        InGameInfo inGameInfo = (InGameInfo) result[0];
        Double average = (Double) result[1];
        return new VoteAvgDTO(
                inGameInfo.getChampionName(), average, inGameInfo.getPosition(), inGameInfo.getTier());
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(INVALID_USER_ID)
        );
    }
}
