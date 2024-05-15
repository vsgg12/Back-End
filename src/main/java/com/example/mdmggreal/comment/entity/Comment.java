package com.example.mdmggreal.comment.entity;

import com.example.mdmggreal.comment.dto.request.CommentAddRequest;
import com.example.mdmggreal.global.entity.BaseEntity;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.post.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@SuperBuilder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Long like;

    public static Comment of(Post post, Member member, Comment comment, CommentAddRequest request) {
        return Comment.builder()
                .content(request.getContent())
                .post(post)
                .parent(comment)
                .member(member)
                .like(0L)
                .build();
    }
    public static Comment of(Post post, Member member, CommentAddRequest request) {
        return Comment.builder()
                .content(request.getContent())
                .post(post)
                .member(member)
                .like(0L)
                .build();
    }
}
