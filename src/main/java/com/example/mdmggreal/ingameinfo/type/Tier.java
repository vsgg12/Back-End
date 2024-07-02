package com.example.mdmggreal.ingameinfo.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Tier {
    CHALLENGER("챌린저"),
    GRANDMASTER("그랜드마스터"),
    MASTER("마스터"),
    DIAMOND("다이아몬드"),
    EMERALD("에메랄드"),
    PLATINUM("플래티넘"),
    GOLD("골드"),
    SILVER("실버"),
    BRONZE("브론즈"),
    IRON("아이언"),
    UNRANK("언랭");

    private final String name;


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
}
