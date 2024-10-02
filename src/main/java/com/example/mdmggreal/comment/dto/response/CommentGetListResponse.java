package com.example.mdmggreal.comment.dto.response;

import com.example.mdmggreal.comment.entity.Comment;
import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.member.dto.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.mdmggreal.global.entity.type.BooleanEnum.TRUE;

@Getter
@SuperBuilder
public class CommentGetListResponse extends BaseResponse {
    private List<CommentDTO> comments;

    public static CommentGetListResponse from(List<CommentDTO> comments, HttpStatus status) {
        return CommentGetListResponse.builder()
                .resultCode(status.value())
                .resultMsg(status.name())
                .comments(comments)
                .build();
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CommentDTO {
        private Long id;
        private String content;
        private MemberDTO member;
        private LocalDateTime createdDateTime;
        private List<CommentDTO> children = new ArrayList<>();

        public static CommentDTO from(Comment comment) {
            return CommentDTO.builder()
                    .id(comment.getId())
                    .content(comment.getIsDeleted().equals(TRUE) ? "삭제된 댓글입니다." : comment.getContent())
                    .member(MemberDTO.from(comment.getMember()))
                    .createdDateTime(comment.getCreatedDateTime())
                    .children(new ArrayList<>())  // children 리스트를 초기화
                    .build();
        }
    }
}
