package com.example.mdmggreal.post.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostStatus {
    FINISHED("판결 종료"),
    PROGRESS("판결 중");

    private final String value;
}
