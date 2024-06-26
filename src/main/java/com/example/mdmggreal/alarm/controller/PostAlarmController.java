package com.example.mdmggreal.alarm.controller;

import com.example.mdmggreal.alarm.dto.AlarmDTO;
import com.example.mdmggreal.alarm.dto.response.AlarmResponse;
import com.example.mdmggreal.alarm.service.PostAlarmService;
import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarm/post")
public class PostAlarmController {

    private final PostAlarmService postAlarmService;

    @GetMapping
    public AlarmResponse postAlarmGet(@RequestHeader(value = "Authorization") String token) {
        Long memberId = JwtUtil.getMemberId(token);
        List<AlarmDTO> postAlarmList = postAlarmService.getPostAlarmList(memberId);
        return AlarmResponse.from(postAlarmList, OK);
    }

    @PatchMapping("/{alarmId}")
    public BaseResponse CommentAlarmModify(@RequestHeader(value = "Authorization") String token, @PathVariable Long alarmId) {
        Long memberId = JwtUtil.getMemberId(token);
        postAlarmService.modifyAlarm(memberId, alarmId);
        return BaseResponse.from(OK);
    }
}
