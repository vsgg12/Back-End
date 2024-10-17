package com.example.mdmggreal.post.repository;

import com.example.mdmggreal.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p " +
            "WHERE p.endDateTime < :dateTime " +
            "AND p.isDeleted = com.example.mdmggreal.global.entity.type.BooleanEnum.FALSE " +
            "AND p.status = com.example.mdmggreal.post.entity.type.PostStatus.PROGRESS")
    List<Post> findEndedActiveProgressPosts(LocalDateTime dateTime);
}
