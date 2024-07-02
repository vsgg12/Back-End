package com.example.mdmggreal.ingameinfo.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Tier {
    CHALLENGER("챌린저", 2000, 1200),
    GRANDMASTER("그랜드마스터", 1000, 600),
    MASTER("마스터", 500, 300),
    DIAMOND("다이아몬드", 300, 180),
    EMERALD("에메랄드", 200, 120),
    PLATINUM("플래티넘", 150, 90),
    GOLD("골드", 100, 60),
    SILVER("실버", 50, 30),
    BRONZE("브론즈", 30, 18),
    IRON("아이언", 1, 1),
    UNRANK("언랭", 0, 0),
    ;
    private final String name;
    private final Integer joinedResult;
    private final Integer predictedResult;




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
