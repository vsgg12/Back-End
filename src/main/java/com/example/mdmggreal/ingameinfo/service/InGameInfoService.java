package com.example.mdmggreal.ingameinfo.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.ingameinfo.dto.response.InGameInfoDTO;
import com.example.mdmggreal.ingameinfo.repository.InGameInfoQueryRepository;
import com.example.mdmggreal.ingameinfo.type.Tier;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberQueryRepository;
import com.example.mdmggreal.post.repository.PostRepository;
import com.example.mdmggreal.post.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InGameInfoService {

    private final InGameInfoQueryRepository inGameInfoQueryRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final PostService postService;
    private final PostRepository postRepository;


    @Transactional
    public void processInGameInfoByPostId(Long postId) {
        List<InGameInfoDTO> inGameInfoDTOList = postService.createInGameInfoDTOList(postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_POST)
        ));
        InGameInfoDTO inGameInfo = inGameInfoDTOList.stream()
                .max(Comparator.comparingDouble(InGameInfoDTO::getAverageRatio))
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_IN_GAME_INFO));
        updateMembers(inGameInfo);
    }

    private void updateMembers(InGameInfoDTO inGameInfo) {
        List<Member> correctMembers = memberQueryRepository.getCorrectMember(inGameInfo.getInGameInfoId(), inGameInfo.getAverageRatio());
        correctMembers.forEach(Member::editPredictedResult);

        List<Member> joinedMembers = memberQueryRepository.getJoinedMember(inGameInfo.getInGameInfoId());
        joinedMembers.forEach(member -> {
            Tier tier = Tier.getTier(member.getJoinedResult(), member.getPredictedResult());
            member.updateTier(tier);
        });
    }
}
