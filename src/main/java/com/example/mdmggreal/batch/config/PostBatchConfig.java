package com.example.mdmggreal.batch.config;

import com.example.mdmggreal.alarm.service.PostAlarmService;
import com.example.mdmggreal.global.entity.type.BooleanEnum;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.post.entity.type.PostStatus;
import com.example.mdmggreal.post.repository.PostRepository;
import com.example.mdmggreal.post.service.PostService;
import com.example.mdmggreal.vote.entity.Vote;
import com.example.mdmggreal.vote.repository.VoteQueryRepository;
import com.example.mdmggreal.vote.service.VoteResultService;
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
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class PostBatchConfig extends DefaultBatchConfiguration {
    private final PostRepository postRepository;
    private final VoteQueryRepository voteQueryRepository;
    private final PostService postService;
    private final PostAlarmService postAlarmService;
    private final VoteResultService voteResultService;

    @Bean
    public Job updatePostJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("updatePostJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(updatePostStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step updatePostStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("updatePostStep", jobRepository)
                .tasklet(updatePostTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet updatePostTasklet() {
        return (contribution, chunkContext) -> {
            LocalDateTime now = LocalDateTime.now();
            List<Post> postList = postRepository.findByEndDateTimeBeforeAndIsDeletedAndStatus(now, BooleanEnum.FALSE, PostStatus.PROGRESS);
            processPostsAfterEndDate(postList);
            return RepeatStatus.FINISHED;
        };
    }

    private void processPostsAfterEndDate(List<Post> postList) {
        postList.forEach(post -> {
            addPostAlarms(post);
            postService.rewardPointByPostCreation(post.getMember());
            voteResultService.findVictoryMembersAndUpdateMembers(post.getId());

            post.editStatus();
        });
    }

    private void addPostAlarms(Post post) {
        // 게시글 작성한 사람 알람
        postAlarmService.addPostedMemberAlarm(post);
        // 게시글 참여한 사람 알람
        List<Long> memberIdsByPostId = voteQueryRepository.getVoteListByPostId(post.getId());
        memberIdsByPostId.forEach(memberId -> postAlarmService.addVotedMemberAlarm(post, memberId));
    }
}