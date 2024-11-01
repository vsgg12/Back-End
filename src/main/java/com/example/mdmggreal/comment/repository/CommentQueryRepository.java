package com.example.mdmggreal.comment.repository;

import com.example.mdmggreal.comment.entity.Comment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.mdmggreal.comment.entity.QComment.comment;
import static com.example.mdmggreal.post.entity.QPost.post;

@Repository
public class CommentQueryRepository extends QuerydslRepositorySupport {

    public CommentQueryRepository() {
        super(Comment.class);
    }

    public List<Comment> getList(Long postId) {
        return from(comment)
                .leftJoin(post)
                .on(comment.post.id.eq(post.id))
                .where(post.id.eq(postId))
                .orderBy(comment.parent.id.asc().nullsFirst(),
                        comment.createdDateTime.desc())
                .fetch();

    }

}
