package com.example.mdmggreal.vote.repository;


import com.example.mdmggreal.post.entity.type.PostStatus;
import com.example.mdmggreal.vote.entity.Vote;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.mdmggreal.ingameinfo.entity.QInGameInfo.inGameInfo;
import static com.example.mdmggreal.member.entity.QMember.member;
import static com.example.mdmggreal.post.entity.QPost.post;
import static com.example.mdmggreal.post.entity.type.PostStatus.PROGRESS;
import static com.example.mdmggreal.vote.entity.QVote.vote;

@Repository
@Slf4j
public class VoteQueryDSLRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public VoteQueryDSLRepository(JPAQueryFactory jpaQueryFactory) {
        super(Vote.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Optional<Vote> getVoteByPostIdAndMemberId(Long postId, Long memberId) {
        return Optional.ofNullable(
                from(vote)
                        .leftJoin(member).on(vote.memberId.eq(memberId))
                        .leftJoin(inGameInfo).on(vote.inGameInfo.id.eq(inGameInfo.id).and(inGameInfo.post.id.eq(postId)))
                        .fetchFirst() // 수정
        );
    }

    public List<Vote> getVoteListByPostId(Long postId) {
        return from(vote)
                .leftJoin(inGameInfo)
                .on(vote.inGameInfo.id.eq(inGameInfo.id))
                .leftJoin(post)
                .on(inGameInfo.post.id.eq(post.id))
                .where(post.id.eq(postId)
                        .and(post.status.eq(PROGRESS)))
                .groupBy(vote.memberId)
                .fetch();
    }

    public boolean existsVoteByMemberId(Long postId, Long memberId) {
        return
                from(vote)
                        .leftJoin(member)
                        .on(vote.memberId.eq(memberId))
                        .leftJoin(inGameInfo)
                        .on(vote.inGameInfo.id.eq(inGameInfo.id))
                        .leftJoin(post)
                        .on(post.id.eq(inGameInfo.post.id))
                        .where(post.id.eq(postId).and(member.id.eq(memberId)))
                        .fetchFirst() != null;
    }
}
