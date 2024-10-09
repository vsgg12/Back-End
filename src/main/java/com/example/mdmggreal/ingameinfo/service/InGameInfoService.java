package com.example.mdmggreal.ingameinfo.service;

import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.ingameinfo.repository.InGameInfoQueryRepository;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberQueryRepository;
import com.example.mdmggreal.member.type.MemberTier;
import com.example.mdmggreal.vote.repository.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InGameInfoService {

    private final InGameInfoQueryRepository inGameInfoQueryRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final VoteRepository voteRepository;


    @Transactional
    public void processInGameInfoByPostId(Long postId) {

        InGameInfo ingameInfo = inGameInfoQueryRepository.getInGameInfoVoteMaxRatioByPostId(postId);
        Long l = voteRepository.countByInGameInfoId(ingameInfo.getId());
        if (l != 0) {
            long averageRatio = ingameInfo.getTotalRatio() / l;
            updateMembers(ingameInfo, averageRatio);
        }
    }

    private void updateMembers(InGameInfo inGameInfo, long l1) {
        List<Member> correctMembers = memberQueryRepository.getCorrectMember(inGameInfo.getId(), l1);
        correctMembers.forEach(Member::editPredictedResult);
        correctMembers.forEach(member -> {
            MemberTier tier = MemberTier.getTier(member.getJoinedResult(), member.getPredictedResult());
            member.updateTier(tier);
        });
    }
}
