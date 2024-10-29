package com.example.mdmggreal.alarm.service;

import com.example.mdmggreal.alarm.dto.AlarmDTO;
import com.example.mdmggreal.alarm.entity.CommentAlarm;
import com.example.mdmggreal.alarm.entity.PostAlarm;
import com.example.mdmggreal.alarm.repository.CommentAlarmRepository;
import com.example.mdmggreal.alarm.repository.PostAlarmRepository;
import com.example.mdmggreal.comment.entity.Comment;
import com.example.mdmggreal.comment.repository.CommentRepository;
import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.service.MemberGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final PostAlarmRepository postAlarmRepository;
    private final CommentAlarmRepository commentAlarmRepository;
    private final CommentRepository commentRepository;
    private final MemberGetService memberGetService;

    public List<AlarmDTO> getAlarmList(Long memberId) {
        Member member = memberGetService.getMemberByIdOrThrow(memberId);
        List<AlarmDTO> alarmListResult = new ArrayList<>();

        // 게시글 알람 목록 조회 후 List에 추가
        alarmListResult.addAll(postAlarmRepository.findByMemberId(member.getId())
                .stream().map(AlarmDTO::from)
                .toList());

        // 댓글 알람 목록 조회 후 List에 추가
        List<CommentAlarm> commentAlarmList = commentAlarmRepository.findByMemberId(member.getId());
        for (CommentAlarm commentAlarm : commentAlarmList) {
            Comment comment = commentRepository.findById(commentAlarm.getComment().getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_EXISTS));

            AlarmDTO commentAlarmDTO = AlarmDTO.of(commentAlarm, comment.getPost().getId());
            alarmListResult.add(commentAlarmDTO);
        }

        return alarmListResult.stream()
                .sorted(Comparator  // 정렬 1순위 : 안 읽은 순, 2순위 : 최근 순
                        .comparing(AlarmDTO::getIsRead)
                        .thenComparing(AlarmDTO::getCreatedDateTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    /**
     * 보관 기간 지난 알람 삭제
     */
    @Transactional
    public void deleteOldAlarms(LocalDateTime alarmStorageTime) {
        List<PostAlarm> oldPostAlarms = postAlarmRepository.findByCreatedDateTimeBefore(alarmStorageTime);
        List<CommentAlarm> oldCommentAlarms = commentAlarmRepository.findByCreatedDateTimeBefore(alarmStorageTime);

        oldPostAlarms.forEach(PostAlarm::delete);
        oldCommentAlarms.forEach(CommentAlarm::delete);
    }
}
