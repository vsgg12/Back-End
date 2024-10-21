package com.example.mdmggreal.member.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberTier {
    CHALLENGER("챌린저", 2000, 1200, 70, 75),
    GRANDMASTER("그랜드마스터", 1000, 600, 60, 65),
    MASTER("마스터", 500, 300, 50, 55),
    DIAMOND("다이아몬드", 300, 180, 40, 45),
    EMERALD("에메랄드", 200, 120, 30, 35),
    PLATINUM("플래티넘", 150, 90, 25, 30),
    GOLD("골드", 100, 60, 20, 25),
    SILVER("실버", 50, 30, 15, 20),
    BRONZE("브론즈", 30, 18, 10, 15),
    UNRANK("언랭", 0, 0, 5,10),
    ;
    private final String name;
    private final Integer joinedResult;         // 참여 판결 수. 충족 시 해당 티어로 승급 가능
    private final Integer predictedResult;      // 맞춘 판결 수. 충족 시 해당 티어로 승급 가능
    private final Integer joinedResultPoint;    // 해당 티어로 판결 참여 시 획득 가능한 포인트
    private final Integer postCreationPoint;    // 해당 티어로 게시글 작성 시 획득 가능한 포인트

    public static MemberTier getNextTier(MemberTier currentTier) {
        MemberTier[] tiers = MemberTier.values();
        if (!currentTier.equals(MemberTier.CHALLENGER)) {
            for (int i = 0; i < tiers.length ; i++) {
                if (tiers[i].joinedResultPoint.equals(currentTier.joinedResultPoint)) {
                    return tiers[i - 1];
                }
            }
        }
        return null;
    }

    public static MemberTier getTier(Integer joinedResult, Integer predictedResult) {
        for (MemberTier memberTier : MemberTier.values()) {
            if (joinedResult >= memberTier.getJoinedResult() && predictedResult >= memberTier.getPredictedResult()) {
                return memberTier;
            }
        }
        throw new IllegalArgumentException("No tier found with the given results.");
    }
}
