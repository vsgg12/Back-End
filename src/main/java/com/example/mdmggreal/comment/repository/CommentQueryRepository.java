package com.example.mdmggreal.comment.repository;

import com.example.mdmggreal.comment.entity.Comment;
import com.example.mdmggreal.comment.entity.QComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    public Optional<Comment> findCommentByIdWithParent(Long commentId) {
        Comment comment = from(QComment.comment)
                .leftJoin(QComment.comment.parent).fetchJoin()
                .where(QComment.comment.id.eq(commentId))
                .fetchOne();
        return Optional.of(comment);
    }
}
