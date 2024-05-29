package com.example.mdmggreal.vote.repository;


import com.example.mdmggreal.vote.entity.Vote;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.example.mdmggreal.ingameinfo.entity.QInGameInfo.inGameInfo;
import static com.example.mdmggreal.member.entity.QMember.member;
import static com.example.mdmggreal.vote.entity.QVote.vote;

@Repository
@Slf4j
public class VoteRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public VoteRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
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

    public boolean existsVoteByMemberId(Long postId, Long memberId) {
        return
                from(vote)
                        .leftJoin(member).on(vote.memberId.eq(memberId))
                        .leftJoin(inGameInfo).on(vote.inGameInfo.id.eq(inGameInfo.id).and(inGameInfo.post.id.eq(postId)))
                        .fetchFirst() != null;
    }
}
