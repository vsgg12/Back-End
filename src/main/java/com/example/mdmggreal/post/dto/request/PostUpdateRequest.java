package com.example.mdmggreal.post.dto.request;

import jakarta.validation.constraints.Pattern;

public record PostUpdateRequest(
        String title,
        @Pattern(regexp = "^\\d{8}$", message = "yyyyMMdd 형식이어야 합니다.") String voteEndDate
) {
}