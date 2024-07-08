package com.example.mdmggreal.alarm.dto;

import com.example.mdmggreal.alarm.entity.CommentAlarm;
import com.example.mdmggreal.alarm.entity.PostAlarm;
import com.example.mdmggreal.alarm.entity.type.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.example.mdmggreal.alarm.entity.type.AlarmType.COMMENT;
import static com.example.mdmggreal.alarm.entity.type.AlarmType.POST;
import static com.example.mdmggreal.global.entity.type.BooleanEnum.TRUE;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDTO {
    private Long alarmId;

    private String alarmContents;

    private AlarmType alarmType;

    // 게시글/댓글 알람과 관련된 게시글의 ID
    private Long postId;

    // 댓글/대댓글 알람일 경우 해당 댓글/대댓글의 내용
    private String commentContent;

    private Boolean isRead;

    private LocalDateTime createdDateTime;

    public static AlarmDTO from(PostAlarm postAlarm) {
        return AlarmDTO.builder()
                .alarmId(postAlarm.getId())
                .alarmContents(postAlarm.getAlarmContents())
                .postId(postAlarm.getPost().getId())
                .alarmType(POST)
                .isRead(postAlarm.getIsRead().equals(TRUE))
                .createdDateTime(postAlarm.getCreatedDateTime())
                .build();
    }

    public static AlarmDTO of(CommentAlarm commentAlarm, Long postId) {
        return AlarmDTO.builder()
                .alarmId(commentAlarm.getId())
                .alarmContents(commentAlarm.getAlarmContents())
                .postId(postId)
                .commentContent(commentAlarm.getComment().getContent())
                .alarmType(COMMENT)
                .isRead(commentAlarm.getIsRead().equals(TRUE))
                .createdDateTime(commentAlarm.getCreatedDateTime())
                .build();
    }
}
