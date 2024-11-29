package com.example.mdmggreal.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommentAddRequest {

    @Comment("부모 댓글 ID")
    private Long parentId;

    @Comment("조상 댓글 ID")
    private Long grandParentId;

    @NotBlank(message = "댓글 내용을 작성해주세요.")
    private String content;
}
