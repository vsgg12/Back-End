package com.example.mdmggreal.hashtag.repository;

import com.example.mdmggreal.hashtag.entity.Hashtag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.mdmggreal.hashtag.entity.QHashtag.hashtag;
import static com.example.mdmggreal.posthashtag.entity.QPostHashtag.postHashtag;

@Repository
@Slf4j
public class HashtagRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public HashtagRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Hashtag.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Hashtag> getListHashtagByPostId(Long postId) {
        return from(hashtag)
                .leftJoin(postHashtag)
                .on(hashtag.id.eq(postHashtag.hashtag.id))
                .where(postHashtag.post.id.eq(postId))
                .fetch();
    }
}