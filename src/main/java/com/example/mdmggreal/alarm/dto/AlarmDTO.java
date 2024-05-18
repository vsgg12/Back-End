package com.example.mdmggreal.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDTO {
    private Long memberId;
    private String alarmContents;
    private String alarmType;
}
