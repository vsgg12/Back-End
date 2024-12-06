package com.example.mdmggreal.member.repository;

import com.example.mdmggreal.member.entity.NicknameUpdateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface NicknameUpdateHistoryRepository extends JpaRepository<NicknameUpdateHistory, Long> {

    @Query("SELECT n.createdDateTime FROM NicknameUpdateHistory n WHERE n.memberId=:memberId")
    LocalDateTime findCreatedDateTimeByMemberId(Long memberId);
}
