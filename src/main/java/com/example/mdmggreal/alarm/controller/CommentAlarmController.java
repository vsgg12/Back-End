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
        Long memberId = JwtUtil.getMemberId(token);
        List<AlarmDTO> commentAlarmList = commentAlarmService.getCommentAlarmList(memberId);
        return AlarmResponse.from(commentAlarmList, OK);
    }

    @PatchMapping("/{alarmId}")
    public BaseResponse CommentAlarmModify(@RequestHeader(value = "Authorization") String token, @PathVariable Long alarmId) {
        Long memberId = JwtUtil.getMemberId(token);
        commentAlarmService.modifyAlarm(memberId, alarmId);

        return BaseResponse.from(OK);
    }
}
