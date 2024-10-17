package com.example.mdmggreal.post.repository;

import com.example.mdmggreal.global.entity.type.BooleanEnum;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.post.entity.type.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByEndDateTimeBeforeAndIsDeletedAndStatus(LocalDateTime dateTime, BooleanEnum isDeleted, PostStatus status);
}
