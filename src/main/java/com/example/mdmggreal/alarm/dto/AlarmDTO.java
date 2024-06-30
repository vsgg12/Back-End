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

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDTO {
    private Long alarmId;
    private String alarmContents;
    private Long postId;
    private Long commentId;
    private AlarmType alarmType;
    private Boolean isRead;
    private LocalDateTime createdDateTime;

    public static AlarmDTO from(PostAlarm postAlarm) {
        return AlarmDTO.builder()
                .alarmId(postAlarm.getId())
                .alarmContents(postAlarm.getAlarmContents())
                .postId(postAlarm.getPost().getId())
                .alarmType(POST)
                .isRead(postAlarm.getIsRead())
                .createdDateTime(postAlarm.getCreatedDateTime())
                .build();
    }

    public static AlarmDTO from(CommentAlarm commentAlarm) {
        return AlarmDTO.builder()
                .alarmId(commentAlarm.getId())
                .alarmContents(commentAlarm.getAlarmContents())
                .commentId(commentAlarm.getComment().getId())
                .alarmType(COMMENT)
                .isRead(commentAlarm.getIsRead())
                .createdDateTime(commentAlarm.getCreatedDateTime())
                .build();
    }
}
