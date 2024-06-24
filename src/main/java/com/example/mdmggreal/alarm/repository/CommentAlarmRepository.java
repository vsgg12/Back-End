package com.example.mdmggreal.alarm.repository;

import com.example.mdmggreal.alarm.entity.CommentAlarm;
import com.example.mdmggreal.alarm.entity.PostAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentAlarmRepository extends JpaRepository<CommentAlarm, Long> {

    List<CommentAlarm> findByMemberId(Long memberId);
    // 한 달 전에 생성된 알람 삭제
    void deleteByCreatedDateTimeBefore(LocalDateTime createdDateTime);
}
