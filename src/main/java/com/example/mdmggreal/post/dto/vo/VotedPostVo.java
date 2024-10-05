package com.example.mdmggreal.post.dto.vo;

import com.example.mdmggreal.global.entity.type.BooleanEnum;
import com.example.mdmggreal.member.type.MemberTier;
import com.example.mdmggreal.post.entity.type.PostStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter // Projections.fields 사용 시 필요
@NoArgsConstructor
public class VotedPostVo {
    private Long postId; // 게시글 id
    private String title; // 게시글 제목
    private Long authorId; // 작성자 id
    private String authorNickname; // 작성자 닉네임
    private MemberTier authorTier; // 작성자 티어
    private PostStatus status; // 게시글 진행 상태
    private LocalDateTime createdDateTime; // 게시글 작성 일시
    private BooleanEnum isDeleted; // 게시글 삭제 여부
    private Long inGameInfoId; // InGameInfo id
    private Integer ratio; // 조회한 회원이 inGameInfo에 판결한 비율
}
