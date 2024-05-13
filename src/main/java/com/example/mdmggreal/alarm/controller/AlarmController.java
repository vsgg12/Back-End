package com.example.mdmggreal.alarm.controller;

import com.example.mdmggreal.alarm.entity.Alarm;
import com.example.mdmggreal.alarm.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @PostMapping("/save")
    public void addAlarm(@RequestBody Alarm alarm) {
        // 테스트용
        alarmService.saveAlarm(alarm);
        alarmService.sendNotification(alarm.getAlarmContents());

        // TODO 댓글, 투표기간 종료 시 alarmService.sendNotification(message); 사용
    }
}
