package com.example.mdmggreal.vote.repository;

import com.example.mdmggreal.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Long countByInGameInfoId(Long inGameInfoId);
}

