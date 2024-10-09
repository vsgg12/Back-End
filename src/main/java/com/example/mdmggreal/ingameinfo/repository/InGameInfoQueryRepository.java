package com.example.mdmggreal.ingameinfo.repository;

import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.example.mdmggreal.ingameinfo.entity.QInGameInfo.inGameInfo;
import static com.example.mdmggreal.vote.entity.QVote.vote;

@Repository
@Slf4j
public class InGameInfoQueryRepository extends QuerydslRepositorySupport {

    public InGameInfoQueryRepository() {
        super(InGameInfo.class);
    }


    public InGameInfo getInGameInfoVoteMaxRatioByPostId(Long postId) {
        return from(inGameInfo)
                .leftJoin(vote).on(inGameInfo.id.eq(vote.inGameInfo.id))
                .where(inGameInfo.post.id.eq(postId))
                .orderBy(inGameInfo.totalRatio.desc())
                .fetchFirst();
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
