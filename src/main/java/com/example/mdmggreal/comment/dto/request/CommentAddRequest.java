package com.example.mdmggreal.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommentAddRequest {

    private Long parentId;

    @NotBlank(message = "댓글 내용을 작성해주세요.")
    private String content;
}
