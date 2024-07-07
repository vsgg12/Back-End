package com.example.mdmggreal.ingameinfo.service;

import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.ingameinfo.repository.InGameInfoQueryRepository;
import com.example.mdmggreal.ingameinfo.type.Tier;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberQueryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class InGameInfoService {

    private final InGameInfoQueryRepository inGameInfoQueryRepository;
    private final MemberQueryRepository memberQueryRepository;


    @Transactional
    public void processInGameInfoByPostId(Long postId) {
        InGameInfo inGameInfo = inGameInfoQueryRepository.getInGameInfoVoteMaxRatioByPostId(postId);
        updateMembers(inGameInfo);
    }

    private void updateMembers(InGameInfo inGameInfo) {
//        List<Member> correctMembers = memberQueryRepository.getCorrectMember(inGameInfo.getId(), inGameInfo.getAverageRatio());
//        correctMembers.forEach(Member::editPredictedResult);
//
//        List<Member> joinedMembers = memberQueryRepository.getJoinedMember(inGameInfo.getId());
//        joinedMembers.forEach(member -> {
//            Tier tier = Tier.getTier(member.getJoinedResult(), member.getPredictedResult());
//            member.updateTier(tier);
//        });
    }
}
