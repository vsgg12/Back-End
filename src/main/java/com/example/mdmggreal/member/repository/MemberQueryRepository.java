package com.example.mdmggreal.member.repository;

import com.example.mdmggreal.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.mdmggreal.member.entity.QMember.member;
import static com.example.mdmggreal.vote.entity.QVote.vote;

@Repository
@Slf4j
public class MemberQueryRepository extends QuerydslRepositorySupport {

    public MemberQueryRepository(JPAQueryFactory jpaQueryFactory) {
        super(Member.class);
    }

    public List<Member> getCorrectMember(Long inGameInfoId, Double avgRatio) {
        return from(member)
                .leftJoin(vote).on(member.id.eq(vote.memberId))
                .where(vote.inGameInfo.id.eq(inGameInfoId)
                        .and(vote.ratio.lt(avgRatio)))
                .fetch();
    }

    public List<Member> getJoinedMember(Long inGameInfoId) {
        return from(member)
                .leftJoin(vote).on(member.id.eq(vote.memberId))
                .where(vote.inGameInfo.id.eq(inGameInfoId))
                .fetch();
    }
}
