package com.example.mdmggreal.alarm.service;

import com.example.mdmggreal.alarm.entity.CommentAlarm;
import com.example.mdmggreal.alarm.repository.CommentAlarmRepository;
import com.example.mdmggreal.comment.entity.Comment;
import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.mdmggreal.global.exception.ErrorCode.INVALID_USER_ID;

@Service
@RequiredArgsConstructor
public class CommentAlarmService {
    private final CommentAlarmRepository commentAlarmRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    // 댓글 알람 생성
    public void addCommentAlarm(Comment addedComment, String commentedNickname) {
        Post post = postRepository.findById(addedComment.getPost().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_POST));

        Member alarmedMember = memberRepository.findById(post.getMember().getId())
                .orElseThrow();

        commentAlarmRepository.save(CommentAlarm.from(addedComment, commentedNickname, alarmedMember));
    }

    // 대댓글 알람 생성
    public void addChildCommentAlarm(Comment addedComment, Comment parentComment, String commentedNickname) {
        Member alarmedMember = memberRepository.findById(parentComment.getMember().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USER_ID));

        commentAlarmRepository.save(CommentAlarm.from(addedComment, commentedNickname, alarmedMember));
    }

    public void modifyAlarm(Long memberId, Long alarmId) {
        Member member = getMemberByMemberId(memberId);
        CommentAlarm commentAlarm = commentAlarmRepository.findById(alarmId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_ALARM)
        );
        if (!commentAlarm.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }

        commentAlarm.editIsRead();

    }

    private Member getMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(INVALID_USER_ID)
        );
    }
}
