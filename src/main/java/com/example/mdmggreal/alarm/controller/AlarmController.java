package com.example.mdmggreal.alarm.controller;

import com.example.mdmggreal.alarm.entity.Alarm;
import com.example.mdmggreal.alarm.service.AlarmService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    /*
     * 테스트
     */
    @PostMapping("/save")
    public void addAlarm(@RequestBody Alarm alarm) {
        // 테스트용
        alarmService.saveAlarm(alarm);
        alarmService.sendNotification(alarm.getAlarmContents());

        // TODO 댓글, 투표기간 종료 시 alarmService.sendNotification(message); 사용
    }
    /*
     * 리스트 가져오기
     */
    @GetMapping("/list")
    public List<Alarm> getAlarmListForUser(HttpSession session) {

        String token = (String) session.getAttribute("token");

        return alarmService.getAlarmListByToken(token);

    }
}
