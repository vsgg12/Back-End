package com.example.mdmggreal.comment.entity;

import com.example.mdmggreal.comment.dto.request.CommentAddRequest;
import com.example.mdmggreal.global.entity.BaseEntity;
import com.example.mdmggreal.global.entity.type.BooleanEnum;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.post.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.example.mdmggreal.global.entity.type.BooleanEnum.FALSE;
import static com.example.mdmggreal.global.entity.type.BooleanEnum.TRUE;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ColumnDefault("FALSE")
    @Column(nullable = false)
    @Enumerated(STRING)
    private BooleanEnum isDeleted;

    // 대댓글 생성
    public static Comment of(Post post, Member member, Comment comment, CommentAddRequest request) {
        return Comment.builder()
                .content(request.getContent())
                .post(post)
                .parent(comment)
                .member(member)
                .isDeleted(FALSE)
                .build();
    }

    // 댓글 생성
    public static Comment of(Post post, Member member, CommentAddRequest request) {
        return Comment.builder()
                .content(request.getContent())
                .post(post)
                .member(member)
                .isDeleted(FALSE)
                .build();
    }

    public void changeIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted ? TRUE : FALSE;
    }
}
