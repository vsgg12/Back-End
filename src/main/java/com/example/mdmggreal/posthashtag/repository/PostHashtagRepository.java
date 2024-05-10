package com.example.mdmggreal.posthashtag.repository;

import com.example.mdmggreal.posthashtag.entity.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {
}
