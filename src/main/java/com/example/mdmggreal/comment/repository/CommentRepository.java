package com.example.mdmggreal.comment.repository;

import com.example.mdmggreal.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    boolean existsByPostIdAndMemberId(Long postId, Long memberId);

    @Query("SELECT COUNT(DISTINCT c.post.id) " +
            "FROM Comment c " +
            "WHERE c.createdDateTime BETWEEN :startOfDay AND :endOfDay " +
            "AND c.member.id = :memberId")
    long countDistinctPostsWithCommentsByMemberAndDate(@Param("memberId") Long memberId,
                                                       @Param("startOfDay") LocalDateTime startOfDay,
                                                       @Param("endOfDay") LocalDateTime endOfDay);

    /*
    특정 댓글의 대댓글 조회
     */
    List<Comment> findByParentId(Long parentId);
}
