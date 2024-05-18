package com.example.mdmggreal.alarm.controller;

import com.example.mdmggreal.alarm.dto.AlarmDTO;
import com.example.mdmggreal.alarm.entity.Alarm;
import com.example.mdmggreal.alarm.service.AlarmService;
import com.example.mdmggreal.global.security.JwtUtil;
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
    public void addAlarm(@RequestBody AlarmDTO alarmDTO, String mobile) {
        // 테스트용
        alarmService.saveAlarm(alarmDTO, mobile);
    }

    /*
     * 리스트 가져오기
     */
    @GetMapping("/list")
    public List<Alarm> getAlarmListForUser(@RequestHeader(value = "Authorization") String token) {
        JwtUtil.validateToken(token);
        String mobile = JwtUtil.getMobile(token);
        return alarmService.getAlarmListByMemberId(mobile);

    }
}
