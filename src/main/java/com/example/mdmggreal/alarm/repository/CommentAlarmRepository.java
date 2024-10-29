package com.example.mdmggreal.alarm.repository;

import com.example.mdmggreal.alarm.entity.CommentAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentAlarmRepository extends JpaRepository<CommentAlarm, Long> {

    List<CommentAlarm> findByMemberId(Long memberId);

    // 보관 기간이 지난 알람 조회
    List<CommentAlarm> findByCreatedDateTimeBefore(LocalDateTime alarmStorageTime);
}
