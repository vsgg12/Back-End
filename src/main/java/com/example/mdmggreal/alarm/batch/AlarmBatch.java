package com.example.mdmggreal.alarm.batch;

import com.example.mdmggreal.alarm.repository.PostAlarmRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class AlarmBatch {

    private final PostAlarmRepository postAlarmRepository;

    public AlarmBatch(PostAlarmRepository postAlarmRepository) {
        this.postAlarmRepository = postAlarmRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Transactional
    public void runScheduledJob() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        postAlarmRepository.deleteByCreatedDateTimeBefore(oneMonthAgo);
        System.out.println("알람이 삭제되었습니다.");
    }


}
