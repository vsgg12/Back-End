package com.example.mdmggreal.ingameinfo.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Tier {
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
    private final Integer joinedResult;
    private final Integer predictedResult;
    private final Integer joinedResultPoint;
    private final Integer postCreationPoint;





    public static Tier fromName(String name) {
        for (Tier tier : Tier.values()) {
            if (tier.getName().equalsIgnoreCase(name)) {
                return tier;
            }
        }
        throw new IllegalArgumentException("No enum constant with name " + name);
    }

    public static String fromTier(Tier tier) {
        for (Tier t : Tier.values()) {
            if (tier.equals(t)) {
                return t.name;
            }
        }
        throw new IllegalArgumentException("해당하는 티어가 없습니다. : " + tier);
    }

    public static Tier getTier(Integer joinedResult, Integer predictedResult) {
        for (Tier tier : Tier.values()) {
            if (joinedResult >= tier.getJoinedResult() && predictedResult >= tier.getPredictedResult()) {
                return tier;
            }
        }
        throw new IllegalArgumentException("No tier found with the given results.");
    }
}
