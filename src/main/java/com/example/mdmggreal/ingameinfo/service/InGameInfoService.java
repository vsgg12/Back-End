package com.example.mdmggreal.ingameinfo.service;

import com.example.mdmggreal.ingameinfo.dto.response.InGameInfoDTO;
import com.example.mdmggreal.ingameinfo.repository.InGameInfoQueryRepository;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberQueryRepository;
import com.example.mdmggreal.member.type.MemberTier;
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
        InGameInfoDTO inGameInfoVoteMaxRatioByPostId = inGameInfoQueryRepository.getInGameInfoVoteMaxRatioByPostId(postId);
        updateMembers(inGameInfoVoteMaxRatioByPostId);
    }

    private void updateMembers(InGameInfoDTO inGameInfo) {
        List<Member> correctMembers = memberQueryRepository.getCorrectMember(inGameInfo.getInGameInfoId(), inGameInfo.getAverageRatio());
        correctMembers.forEach(Member::editPredictedResult);
        correctMembers.forEach(member -> {
            MemberTier tier = MemberTier.getTier(member.getJoinedResult(), member.getPredictedResult());
            member.updateTier(tier);
        });
    }
}
