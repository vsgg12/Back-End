package com.example.mdmggreal.ingameinfo.repository;

import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.example.mdmggreal.ingameinfo.entity.QInGameInfo.inGameInfo;
import static com.example.mdmggreal.vote.entity.QVote.vote;

@Repository
@Slf4j
public class InGameInfoQueryRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public InGameInfoQueryRepository(JPAQueryFactory jpaQueryFactory) {
        super(InGameInfo.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public InGameInfo getInGameInfoVoteMaxRatioByPostId(Long postId) {

        return from(inGameInfo)
                .where(inGameInfo.post.id.eq(postId))
                .orderBy(vote.ratio.desc())
                .fetchOne();
    }

    public Double getAverageRatioByPostId(Long inGameInfoId) {

        return from(inGameInfo)
                .select(vote.ratio.avg())
                .leftJoin(vote)
                .on(vote.inGameInfo.id.eq(inGameInfo.id))
                .where(inGameInfo.id.eq(inGameInfoId))
                .fetchOne();


    }
}
