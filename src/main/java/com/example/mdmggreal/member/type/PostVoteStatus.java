package com.example.mdmggreal.member.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostVoteStatus {
    IN_PROGRESS("판결 중"),
    FINISH("판결 종료");

    private final String value;
}
