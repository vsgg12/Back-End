package com.example.mdmggreal.vote.service;

import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.ingameinfo.repository.InGameInfoRepository;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberQueryRepository;
import com.example.mdmggreal.member.type.MemberTier;
import com.example.mdmggreal.vote.entity.Vote;
import com.example.mdmggreal.vote.repository.VoteQueryRepository;
import com.example.mdmggreal.vote.type.VoteResultType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.mdmggreal.vote.type.VoteResultType.*;

@RequiredArgsConstructor
@Service
public class VoteResultService {

    private final VoteQueryRepository voteQueryRepository;
    private final InGameInfoRepository inGameInfoRepository;
    private final MemberQueryRepository memberQueryRepository;

    /**
     * 게시글에 투표한 회원 중 승리한 회원들을 찾고, 회원의 상태를 변경
     */
    @Transactional
    public void findVictoryMembersAndUpdateMembers(Long postId) {
        List<InGameInfo> inGameInfoList = inGameInfoRepository.findByPostId(postId);
        List<Long> maxRatioInGameInfoList = getMaxRatioInGameInfoIdList(inGameInfoList); // 최대 투표를 받은 inGameInfo 목록

        // 전부 최대 투표 값인 경우는 무승부. 승리자 없으므로 리턴.
        if (inGameInfoList.size() == maxRatioInGameInfoList.size()) return;

        // 게시글에 투표한 회원 목록
        List<Member> votedMemberList = memberQueryRepository.getVotedMemberListByPostId(postId);
        if (votedMemberList.isEmpty()) return;

        votedMemberList.forEach(member -> {
                    VoteResultType memberVoteResult = calculateMemberResultByPostId(postId, member.getId());
                    if (memberVoteResult.equals(VICTORY)) updateMembersPredictedResultAndTier(member);
                }
        );
    }

    /*
     * List<InGameInfo> 중 최대 투표 값을 가진 inGameInfo id의 list 찾기
     */
    private List<Long> getMaxRatioInGameInfoIdList(List<InGameInfo> inGameInfoList) {
        Long maxTotalRatio = inGameInfoList.stream()
                .map(InGameInfo::getTotalRatio)
                .max(Long::compare)
                .orElse(0L);

        return inGameInfoList.stream()
                .filter(inGameInfo -> inGameInfo.getTotalRatio().equals(maxTotalRatio))
                .map(InGameInfo::getId)
                .toList();
    }

    /**
     * 게시글 1개에 대한 회원의 판결 결과 계산
     */
    public VoteResultType calculateMemberResultByPostId(Long postId, Long votedMemberId) {
        // 게시글의 모든 inGameInfo
        List<InGameInfo> inGameInfoList = inGameInfoRepository.findByPostId(postId);

        // 게시글의 최대 판결 inGameInfo id 목록
        List<Long> maxInGameInfoList = getMaxRatioInGameInfoIdList(inGameInfoList);

        // 모든 inGameInfo 결과가 같으면 '무승부' 반환
        if (inGameInfoList.size() == maxInGameInfoList.size()) {
            return DRAW;
        }

        // 무승부가 아닌 경우
        // 게시글에 대한 회원의 투표 정보
        List<Vote> voteList = voteQueryRepository.getVoteListByMemberIdAndPostId(votedMemberId, postId);
        Map<Long, Long> memberInGameInfoMap = voteList.stream()
                .collect(Collectors.toMap(vote -> vote.getInGameInfo().getId(),
                        vote -> vote.getRatio().longValue())
                );

        // 회원의 최대 판결 inGameInfo 목록
        List<Long> maxMemberVotedInGameInfoList = getMaxInGameInfoList(memberInGameInfoMap);

        // 회원의 최종 판결 결과
        // 회원의 최대 판결 inGameInfo 목록과 게시글의 최대 판결 inGameInfo 중 한개라도 일치하면 승리
        return maxInGameInfoList.stream().anyMatch(maxMemberVotedInGameInfoList::contains) ? VICTORY : DEFEAT;
    }

    /*
     * Map<inGameInfoId, ratio> 중 최대 판결 inGameInfo id 목록
     */
    private List<Long> getMaxInGameInfoList(Map<Long, Long> inGameInfoMap) {
        List<Long> maxInGameInfoList = new ArrayList<>();
        Long maxRatio = 0L;

        for (Map.Entry<Long, Long> entry : inGameInfoMap.entrySet()) {
            Long inGameInfoId = entry.getKey();
            Long ratio = entry.getValue();

            if (maxRatio < ratio) { // 최대 값보다 크면 최대값 갱신 후, maxInGameInfoList 초기화 후 요소 추가
                maxRatio = ratio;
                maxInGameInfoList.clear();
                maxInGameInfoList.add(inGameInfoId);
            } else if (maxRatio.equals(ratio)) { // 최대 값과 같으면 maxInGameInfoList 요소 추가
                maxInGameInfoList.add(inGameInfoId);
            }
        }

        return maxInGameInfoList;
    }


    /*
     * 회원의 predictedResult, memberTier 업데이트
     */
    private void updateMembersPredictedResultAndTier(Member member) {
        member.increasePredictedResult(); // 주의! 순서 변경 금지. 이게 먼저 와야 newTier를 제대로 계산할 수 있음.

        MemberTier newTier = MemberTier.getTier(member.getJoinedResult(), member.getPredictedResult());
        member.updateTier(newTier);
    }

}
