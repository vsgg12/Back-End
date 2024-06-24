package com.example.mdmggreal.alarm.controller;

import com.example.mdmggreal.alarm.dto.AlarmDTO;
import com.example.mdmggreal.alarm.dto.response.AlarmResponse;
import com.example.mdmggreal.alarm.service.CommentAlarmService;
import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/alarm/comment")
@RequiredArgsConstructor
public class CommentAlarmController {

    private final CommentAlarmService commentAlarmService;

    @GetMapping
    public AlarmResponse CommentAlarmGet(@RequestHeader(value = "Authorization") String token) {
        JwtUtil.validateToken(token);
        String mobile = JwtUtil.getMobile(token);
        List<AlarmDTO> commentAlarmList = commentAlarmService.getCommentAlarmList(mobile);
        return AlarmResponse.from(commentAlarmList, OK);
    }

    @PatchMapping("/{alarmId}")
    public BaseResponse CommentAlarmModify(@RequestHeader(value = "Authorization") String token, @PathVariable Long alarmId) {
        JwtUtil.validateToken(token);
        String mobile = JwtUtil.getMobile(token);
        commentAlarmService.modifyAlarm(mobile, alarmId);

        return BaseResponse.from(OK);
    }
}
