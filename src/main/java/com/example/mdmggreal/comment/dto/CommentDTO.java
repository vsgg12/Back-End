package com.example.mdmggreal.comment.dto;

import com.example.mdmggreal.comment.entity.Comment;
import com.example.mdmggreal.member.dto.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.mdmggreal.global.entity.type.BooleanEnum.TRUE;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
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
