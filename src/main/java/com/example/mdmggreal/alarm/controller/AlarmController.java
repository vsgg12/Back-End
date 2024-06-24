package com.example.mdmggreal.alarm.controller;

import com.example.mdmggreal.alarm.dto.AlarmDTO;
import com.example.mdmggreal.alarm.dto.response.AlarmResponse;
import com.example.mdmggreal.alarm.service.AlarmService;
import com.example.mdmggreal.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping
    public AlarmResponse alarmGet(@RequestHeader(value = "Authorization") String token) {
        JwtUtil.validateToken(token);
        String mobile = JwtUtil.getMobile(token);
        List<AlarmDTO> alarmList = alarmService.getAlarmList(mobile);
        return AlarmResponse.from(alarmList, OK);
    }

}
