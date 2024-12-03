package com.example.mdmggreal.comment.repository;

import com.example.mdmggreal.comment.entity.Comment;
import com.example.mdmggreal.comment.entity.QComment;
import com.example.mdmggreal.global.entity.type.BooleanEnum;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.mdmggreal.comment.entity.QComment.comment;
import static com.example.mdmggreal.post.entity.QPost.post;

@Repository
public class CommentQueryRepository extends QuerydslRepositorySupport {

    public CommentQueryRepository() {
        super(CommentQueryRepository.class);
    }

    public List<Comment> getList(Long postId) {
        // 부모 댓글이 없는 최상위 댓글들은 최신순 정렬
        OrderSpecifier<?> orderByParentNull = new OrderSpecifier<>(
                Order.DESC,
                new CaseBuilder()
                        .when(comment.parent.id.isNull())
                        .then(comment.createdDateTime)
                        .otherwise((LocalDateTime) null)
        );

        // 부모 댓글이 있는 댓글들은 작성순 정렬
        OrderSpecifier<?> orderByParentNotNull = new OrderSpecifier<>(
                Order.ASC,
                new CaseBuilder()
                        .when(comment.parent.id.isNotNull())
                        .then(comment.createdDateTime)
                        .otherwise((LocalDateTime) null)
        );

        return from(comment)
                .where(comment.post.id.eq(postId)
                        .and(comment.isDeleted.eq(BooleanEnum.FALSE)))
                .orderBy(orderByParentNull, orderByParentNotNull)
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
