package com.example.mdmggreal.post.repository;

import com.example.mdmggreal.global.entity.type.BooleanEnum;
import com.example.mdmggreal.member.dto.response.PostsByMemberGetResponse;
import com.example.mdmggreal.post.dto.vo.VotedPostVo;
import com.example.mdmggreal.post.entity.Post;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.mdmggreal.comment.entity.QComment.comment;
import static com.example.mdmggreal.ingameinfo.entity.QInGameInfo.inGameInfo;
import static com.example.mdmggreal.member.entity.QMember.member;
import static com.example.mdmggreal.post.entity.QPost.post;
import static com.example.mdmggreal.vote.entity.QVote.vote;

@Repository
@Slf4j
public class PostQueryRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public PostQueryRepository(JPAQueryFactory jpaQueryFactory) {
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
            predicate.and(post.isDeleted.eq(BooleanEnum.FALSE));
        }

        return predicate;
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

    public Page<PostsByMemberGetResponse.MyPost> getPostsByMemberWithPagination(Long memberId, Pageable pageable) {
        // 총 게시글 수 구하기
        Long totalPostsCount = jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(post.member.id.eq(memberId))
                .fetchOne();
        // 게시글이 없는 경우
        if (totalPostsCount == null) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        List<Tuple> tuples = jpaQueryFactory
                .select(
                        post.id,
                        post.title,
                        comment.count(),
                        post.status,
                        post.createdDateTime
                )
                .from(post)
                .leftJoin(comment).on(comment.post.id.eq(post.id))
                .where(post.member.id.eq(memberId)
                        .and(post.isDeleted.eq(BooleanEnum.FALSE)))
                .groupBy(post.id)
                .orderBy(post.createdDateTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<PostsByMemberGetResponse.MyPost> posts = tuples.stream().map(
                        tuple -> PostsByMemberGetResponse.MyPost.builder()
                                .id(tuple.get(post.id))
                                .title(tuple.get(post.title))
                                .commentNum(tuple.get(comment.count()))
                                .createdDateTime(tuple.get(post.createdDateTime))
                                .voteStatus(tuple.get(post.status))
                                .build())
                .toList();

        return new PageImpl<>(posts, pageable, totalPostsCount);
    }

    public Page<VotedPostVo> getVotedPostsByMemberIdPagination(Long memberId, Pageable pageable) {
        // 판결 게시글 조회 기한(현재 판결종료일+30일 - 24.09.30)
        LocalDateTime viewEndDateTime = LocalDate.now().plusDays(31).atStartOfDay();

        List<VotedPostVo> votedPostVoList = jpaQueryFactory.select(
                        Projections.fields(VotedPostVo.class,
                                post.id.as("postId"),
                                post.title,
                                member.id.as("authorId"),
                                member.nickname.as("authorNickname"),
                                member.memberTier.as("authorTier"),
                                post.status,
                                post.createdDateTime,
                                post.isDeleted,
                                inGameInfo.id.as("inGameInfoId"),
                                vote.ratio
                        )
                ).from(post)
                .innerJoin(inGameInfo).on(post.id.eq(inGameInfo.post.id))
                .innerJoin(vote).on(inGameInfo.id.eq(vote.inGameInfo.id)
                        .and(vote.memberId.eq(memberId)))
                .innerJoin(member).on(post.member.id.eq(member.id))
                .where(post.endDateTime.lt(viewEndDateTime))
                .groupBy(post.id, post.createdDateTime)
                .orderBy(post.createdDateTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 게시글이 없는 경우
        if (votedPostVoList.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        return new PageImpl<>(votedPostVoList, pageable, votedPostVoList.size());
    }

}
