package com.example.mdmggreal.alarm.controller;

import com.example.mdmggreal.alarm.service.PostAlarmService;
import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarm/post")
public class PostAlarmController {

    private final PostAlarmService postAlarmService;

    @PatchMapping("/{alarmId}")
    public ResponseEntity<BaseResponse> CommentAlarmModify(@RequestHeader(value = "Authorization") String token, @PathVariable Long alarmId) {
        Long memberId = JwtUtil.getMemberId(token);
        postAlarmService.modifyAlarm(memberId, alarmId);
        return BaseResponse.toResponseEntity(OK);
    }
}
