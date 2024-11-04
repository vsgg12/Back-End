package com.example.mdmggreal.alarm.service;

import com.example.mdmggreal.alarm.entity.CommentAlarm;
import com.example.mdmggreal.alarm.repository.CommentAlarmRepository;
import com.example.mdmggreal.comment.entity.Comment;
import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.service.MemberGetService;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentAlarmService {
    private final CommentAlarmRepository commentAlarmRepository;
    private final PostRepository postRepository;
    private final MemberGetService memberGetService;

    // 댓글 알람 생성
    public void addCommentAlarm(Comment addedComment, String commentedNickname) {
        Post post = postRepository.findById(addedComment.getPost().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_POST));

        Member alarmedMember = memberGetService.getMemberByIdOrThrow(post.getMember().getId());

        commentAlarmRepository.save(CommentAlarm.from(addedComment, commentedNickname, alarmedMember));
    }

    // 대댓글 알람 생성
    public void addChildCommentAlarm(Comment addedComment, Comment parentComment, String commentedNickname) {
        Member alarmedMember = memberGetService.getMemberByIdOrThrow(parentComment.getMember().getId());

        commentAlarmRepository.save(CommentAlarm.from(addedComment, commentedNickname, alarmedMember));
    }

    @Transactional
    public void modifyAlarm(Long memberId, Long alarmId) {
        Member member = memberGetService.getMemberByIdOrThrow(memberId);
        CommentAlarm commentAlarm = commentAlarmRepository.findById(alarmId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_ALARM)
        );
        if (!commentAlarm.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }

        commentAlarm.editIsRead();
    }

}
