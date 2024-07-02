package com.example.mdmggreal.vote.repository;

import com.example.mdmggreal.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("SELECT v.inGameInfo, AVG(v.ratio)" +
            " FROM Vote v " +
            " WHERE v.inGameInfo.post.id = :postId" +
            " GROUP BY v.inGameInfo")
    List<Object[]> findChampionNamesWithAverageRatioByPostId(@Param("postId") Long postId);

    List<Vote> findByMemberId(Long memberId);

    Optional<Vote> findByMemberIdAndInGameInfoId(Long memberId, Long inGameInfoId);
}

