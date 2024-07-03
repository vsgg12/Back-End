package com.example.mdmggreal.alarm.controller;

import com.example.mdmggreal.alarm.service.CommentAlarmService;
import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/alarm/comment")
@RequiredArgsConstructor
public class CommentAlarmController {

    private final CommentAlarmService commentAlarmService;

    @PatchMapping("/{alarmId}")
    public BaseResponse commentAlarmModify(@RequestHeader(value = "Authorization") String token, @PathVariable Long alarmId) {
        Long memberId = JwtUtil.getMemberId(token);
        commentAlarmService.modifyAlarm(memberId, alarmId);

        return BaseResponse.from(OK);
    }
}
