package com.example.mdmggreal.alarm.service;

import com.example.mdmggreal.alarm.dto.AlarmDTO;
import com.example.mdmggreal.alarm.entity.CommentAlarm;
import com.example.mdmggreal.alarm.repository.CommentAlarmRepository;
import com.example.mdmggreal.comment.dto.request.CommentAddRequest;
import com.example.mdmggreal.comment.entity.Comment;
import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.mdmggreal.global.exception.ErrorCode.INVALID_USER_ID;

@Service
@RequiredArgsConstructor
public class CommentAlarmService {
    private final CommentAlarmRepository commentAlarmRepository;
    private final MemberRepository memberRepository;

    public void addAlarm(Comment comment, CommentAddRequest request) {
        commentAlarmRepository.save(CommentAlarm.from(comment, request));
    }

    @Transactional
    public List<AlarmDTO> getCommentAlarmList(Long memberId) {
        Member member = getMemberByMemberId(memberId);
        return commentAlarmRepository.findByMemberId(member.getId()).stream()
                .map(AlarmDTO::from)
                .toList();
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
