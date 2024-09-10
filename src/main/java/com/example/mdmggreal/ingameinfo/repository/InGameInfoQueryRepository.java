package com.example.mdmggreal.ingameinfo.repository;

import com.example.mdmggreal.ingameinfo.dto.response.InGameInfoDTO;
import com.example.mdmggreal.ingameinfo.dto.response.QInGameInfoDTO;
import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.ingameinfo.entity.QInGameInfo;
import com.example.mdmggreal.vote.entity.QVote;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
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

    public InGameInfoDTO getInGameInfoVoteMaxRatioByPostId(Long postId) {
        QInGameInfo inGameInfo = QInGameInfo.inGameInfo;
        QVote vote = QVote.vote;

        NumberPath<Double> averageRatio = Expressions.numberPath(Double.class, "averageRatio");

        QInGameInfoDTO qInGameInfoDTO = new QInGameInfoDTO(
                inGameInfo.id,
                inGameInfo.inGameTier,
                inGameInfo.position,
                inGameInfo.championName,
                ExpressionUtils.as(
                        JPAExpressions
                                .select((vote.ratio.avg()).max())
                                .from(vote)
                                .where(vote.inGameInfo.id.eq(inGameInfo.id))
                                .groupBy(vote.inGameInfo.id),
                        "averageRatio"));


        return from(inGameInfo)
                .select(qInGameInfoDTO)
                .leftJoin(vote).on(inGameInfo.id.eq(vote.inGameInfo.id))
                .where(inGameInfo.post.id.eq(postId))
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
