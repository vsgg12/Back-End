package com.example.mdmggreal.alarm.dto.reqeust;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PostAlarmRequest {
    private Long memberId;
    private Long postId;
    private String alarmContents;

}
