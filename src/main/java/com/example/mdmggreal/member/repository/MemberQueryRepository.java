package com.example.mdmggreal.member.repository;

import com.example.mdmggreal.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.mdmggreal.ingameinfo.entity.QInGameInfo.inGameInfo;
import static com.example.mdmggreal.member.entity.QMember.member;
import static com.example.mdmggreal.post.entity.QPost.post;
import static com.example.mdmggreal.post.entity.type.PostStatus.FINISHED;
import static com.example.mdmggreal.vote.entity.QVote.vote;

@Repository
@Slf4j
public class MemberQueryRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public MemberQueryRepository(JPAQueryFactory jpaQueryFactory) {
        super(Member.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    /**
     * 게시글에 투표한 회원 목록
     * 주의 : 게시글이 종료된 상태에서 조회해야 함!!
     */
    public List<Member> getVotedMemberListByPostId(Long postId) {
        return jpaQueryFactory.select(member)
                .from(member)
                .leftJoin(vote)
                .on(vote.memberId.eq(member.id))
                .leftJoin(inGameInfo)
                .on(inGameInfo.id.eq(vote.inGameInfo.id))
                .leftJoin(post)
                .on(post.id.eq(inGameInfo.post.id))
                .where(post.id.eq(postId)
                        .and(post.status.eq(FINISHED)))
                .groupBy(member.id)
                .fetch();
    }
}
