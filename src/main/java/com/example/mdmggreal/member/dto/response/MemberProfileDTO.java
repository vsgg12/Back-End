package com.example.mdmggreal.member.dto.response;

import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.type.MemberTier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberProfileDTO {
    private Long id;
    private String nickName;
    private Integer point;
    private Integer joinedResult;
    private Integer nextJoinedResult;
    private Integer predicateResult;
    private Integer nextPredicateResult;
    private MemberTier tier;

    public static MemberProfileDTO from(Member member) {
        MemberTier nextTier = MemberTier.getNextTier(member.getMemberTier());
        assert nextTier != null;
        return MemberProfileDTO.builder()
                .id(member.getId())
                .point(member.getPoint())
                .joinedResult(member.getJoinedResult())
                .nextJoinedResult(nextTier.getJoinedResult())
                .predicateResult((member.getPredictedResult()))
                .nextPredicateResult(nextTier.getPredictedResult())
                .tier(member.getMemberTier())
                .nickName(member.getNickname())
                .build();

    }
}
