package com.example.mdmggreal.vote.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VoteResultType {
    VICTORY("승리"),
    DEFEAT("패배"),
    DRAW("무승부");

    private final String value;
}
