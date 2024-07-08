package com.example.mdmggreal.ingameinfo.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InGameTier {
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
    UNRANK("언랭"),
    ;
    private final String name;


    public static InGameTier fromName(String name) {
        for (InGameTier inGameTier : InGameTier.values()) {
            if (inGameTier.getName().equalsIgnoreCase(name)) {
                return inGameTier;
            }
        }
        return null;
    }
}
