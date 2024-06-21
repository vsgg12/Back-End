package com.example.mdmggreal.ingameinfo.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Position {
    TOP("탑"),
    MID("미드"),
    JUNGLE("정글"),
    ADCARRY("원딜"),
    SUPPORT("서폿");
    private String name;

    public static Position fromName(String name) {
        for (Position position : Position.values()) {
            if (position.getName().equalsIgnoreCase(name)) {
                return position;
            }
        }
        throw new IllegalArgumentException("No enum constant with name " + name);
    }
}
