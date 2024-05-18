package com.example.mdmggreal.alarm.controller;

import com.example.mdmggreal.alarm.entity.Alarm;
import com.example.mdmggreal.alarm.service.AlarmService;
import com.example.mdmggreal.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    /*
     * 리스트 가져오기
     */
    @GetMapping("/users")
    public List<Alarm> getAlarmListForUser(@RequestHeader(value = "Authorization") String token) {
        JwtUtil.validateToken(token);
        String mobile = JwtUtil.getMobile(token);
        return alarmService.getAlarmListByMemberId(mobile);

    }
}
