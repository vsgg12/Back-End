package com.example.mdmggreal.batch.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;
    private final Job deleteOldAlarmsJob;
    private final Job updatePostJob;

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor jobProcessor = new JobRegistryBeanPostProcessor();
        jobProcessor.setJobRegistry(jobRegistry);
        return jobProcessor;
    }


    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void runDeleteBatchJob() throws Exception {
        jobLauncher.run(deleteOldAlarmsJob, new JobParametersBuilder()
                .addDate("timestamp", new Date())
                .toJobParameters());
    }


    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
//    @Scheduled(cron = "0/10 * * * * *")
    public void runFinishPostBatchJob() throws Exception {
        jobLauncher.run(updatePostJob, new JobParametersBuilder()
                .addDate("timestamp", new Date())
                .toJobParameters());
    }
}
