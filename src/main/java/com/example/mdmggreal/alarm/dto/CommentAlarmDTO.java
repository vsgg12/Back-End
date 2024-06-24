package com.example.mdmggreal.alarm.dto;

import com.example.mdmggreal.alarm.entity.CommentAlarm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentAlarmDTO {
    private Long alarmId;
    private String alarmContents;
    private Long commentId;

    public static CommentAlarmDTO from(CommentAlarm commentAlarm) {
        return CommentAlarmDTO.builder()
                .alarmId(commentAlarm.getId())
                .alarmContents(commentAlarm.getAlarmContents())
                .commentId(commentAlarm.getId())
                .build();
    }

}
