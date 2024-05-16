package com.example.mdmggreal.vote.repository;

import com.example.mdmggreal.vote.dto.VoteStatisticsDTO;
import com.example.mdmggreal.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("SELECT new com.example.mdmggreal.vote.dto.VoteStatisticsDTO(b.championName, COALESCE(AVG(c.ratio), 0.0)) " +
            "FROM InGameInfo b " +
            "LEFT JOIN b.votes c " +
            "WHERE b.post.id = :postId " +
            "GROUP BY b.championName")
    List<VoteStatisticsDTO> findChampionNamesWithAverageRatioByPostId(@Param("postId") Long postId);

    List<Vote> findByMemberId(Long memberId);
}

