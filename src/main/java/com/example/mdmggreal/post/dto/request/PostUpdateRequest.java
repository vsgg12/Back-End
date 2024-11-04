package com.example.mdmggreal.post.dto.request;

import com.example.mdmggreal.post.entity.type.VideoType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PostUpdateRequest(
        @Schema(description = "게시글 제목")
        String title,

        @Schema(description = "영상 타입")
        VideoType videoType,

        @Schema(description = "영상 링크")
        String videoLink,

        @Schema(description = "판결 기간")
        @Pattern(regexp = "^\\d{8}$", message = "yyyyMMdd 형식이어야 합니다.") String voteEndDate,

        @Schema(description = "해시태그")
        List<@Size(max = 12, message = "공백포함 12자까지 작성가능합니다.") String> hashtag
) {
}