package com.example.mdmggreal.alarm.batch;

import com.example.mdmggreal.alarm.repository.AlarmRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class AlarmBatch {

    private final AlarmRepository alarmRepository;

    public AlarmBatch(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Transactional
    public void runScheduledJob() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        alarmRepository.deleteByCreatedDateTimeBefore(oneMonthAgo);
        System.out.println("알람이 삭제되었습니다.");
    }


}
