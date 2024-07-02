package com.example.mdmggreal.ingameinfo.repository;

import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InGameInfoRepository extends JpaRepository<InGameInfo, Long> {

    List<InGameInfo> findByPostId(Long postId);

    void findInGameInfoVoteMaxRatioByPostId(Long postId);
}
