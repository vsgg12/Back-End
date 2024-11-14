package com.example.mdmggreal.comment.repository;

import com.example.mdmggreal.comment.entity.Comment;
import com.example.mdmggreal.comment.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.mdmggreal.comment.entity.QComment.comment;
import static com.example.mdmggreal.post.entity.QPost.post;

@Repository
public class CommentQueryRepository extends QuerydslRepositorySupport {

    public CommentQueryRepository() {
        super(CommentQueryRepository.class);
    }

    // TOBE - 댓글 전체 조회 시 나오는 데이터
    // 5,1
    // 8,7,6
    // 9,4,3,2

    // grandParent 가 null(게시글의 원 댓글) 인 것이 먼저 온 후 desc 정렬
    // grandParent 가 같으면 최신 순 정렬
    public List<Comment> getList(Long postId) {
        return from(comment)
                .leftJoin(post)
                .on(comment.post.id.eq(post.id))
                .where(post.id.eq(postId))
                .orderBy(comment.grandParent.id.desc().nullsFirst(),
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
