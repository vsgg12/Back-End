package com.example.mdmggreal.post.repository;

import com.example.mdmggreal.post.entity.Post;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.mdmggreal.member.entity.QMember.member;
import static com.example.mdmggreal.post.entity.QPost.post;

@Repository
@Slf4j
public class PostRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public PostRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Post.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Post> getPostList(String orderBy, String keyword) {

        if (orderBy.equals("view")) {
            return jpaQueryFactory.from(post)
                    .select(post)
                    .orderBy(post.viewCount.desc())
                    .where(getPostListPredicate(keyword))
                    .fetch();
        } else {
            return jpaQueryFactory.from(post)
                    .select(post)
                    .orderBy(post.createdDateTime.desc())
                    .where(getPostListPredicate(keyword))
                    .fetch();
        }

    }
public Predicate getPostListPredicate(String keyword) {
    BooleanBuilder predicate = new BooleanBuilder();
    if (keyword != null && !keyword.isEmpty()) {
        predicate.and(post.content.containsIgnoreCase(keyword).or(
                post.title.containsIgnoreCase(keyword)
        ));
    }

    return predicate;
}
    public List<Post> getPostsMember(Long id) {
        return jpaQueryFactory.from(post)
                .leftJoin(member)
                .on(post.member.id.eq(member.id))
                .select(post)
                .where(member.id.eq(id))
                .orderBy(post.createdDateTime.desc())
                .fetch();

    }


    public List<Post> getPostsKeyword(String keyWord) {
        return jpaQueryFactory.from(post)
                .leftJoin(member)
                .on(post.member.id.eq(member.id))
                .select(post)
                .where(post.content.containsIgnoreCase(keyWord)
                        .or(post.title.containsIgnoreCase(keyWord)))
                .orderBy(post.createdDateTime.desc())
                .fetch();

    }
}
