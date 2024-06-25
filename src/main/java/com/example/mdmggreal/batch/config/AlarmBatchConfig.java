package com.example.mdmggreal.batch.config;

import com.example.mdmggreal.alarm.repository.CommentAlarmRepository;
import com.example.mdmggreal.alarm.repository.PostAlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class AlarmBatchConfig extends DefaultBatchConfiguration {

    private final PostAlarmRepository postAlarmRepository;
    private final CommentAlarmRepository commentAlarmRepository;

    @Bean
    public Job deleteOldAlarmsJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("deleteOldAlarmsJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(deleteOldAlarmsStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step deleteOldAlarmsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("deleteOldAlarmsStep", jobRepository)
                .tasklet(deleteOldAlarmsTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet deleteOldAlarmsTasklet() {
        return ((contribution, chunkContext) -> {
            LocalDateTime fourteenDaysAgo = LocalDateTime.now().minusDays(14);
            postAlarmRepository.deleteByCreatedDateTimeBefore(fourteenDaysAgo);
            commentAlarmRepository.deleteByCreatedDateTimeBefore(fourteenDaysAgo);
            System.out.println("한 달이 지난 알람이 삭제되었습니다.");
            return RepeatStatus.FINISHED;
        });
    }

}
