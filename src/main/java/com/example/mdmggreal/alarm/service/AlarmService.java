package com.example.mdmggreal.alarm.service;

import com.example.mdmggreal.alarm.dto.AlarmDTO;
import com.example.mdmggreal.alarm.dto.reqeust.PostAlarmRequest;
import com.example.mdmggreal.alarm.entity.PostAlarm;
import com.example.mdmggreal.alarm.repository.CommentAlarmRepository;
import com.example.mdmggreal.alarm.repository.PostAlarmRepository;
import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.mdmggreal.global.exception.ErrorCode.INVALID_USER_ID;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final PostAlarmRepository postAlarmRepository;
    private final MemberRepository memberRepository;
    private final CommentAlarmRepository commentAlarmRepository;


    public List<AlarmDTO> getAlarmList(String mobile) {
        Member member = getMember(mobile);
        List<AlarmDTO> alarmDTOList = new ArrayList<>();

        alarmDTOList.addAll(postAlarmRepository.findByMemberId(member.getId())
                .stream().map(AlarmDTO::from)
                .toList());

        alarmDTOList.addAll(commentAlarmRepository.findByMemberId(member.getId())
                .stream().map(AlarmDTO::from)
                .toList());

        return alarmDTOList.stream()
                .sorted(Comparator.comparing(AlarmDTO::getCreatedDateTime).reversed())
                .collect(Collectors.toList());

    }

    public List<AlarmDTO> getPostAlarmList(String mobile) {
        Member member = getMember(mobile);
        return postAlarmRepository.findByMemberId(member.getId()).stream()
                .map(AlarmDTO::from)
                .toList();
    }

    public List<AlarmDTO> getCommentAlarmList(String mobile) {
        Member member = getMember(mobile);
        return commentAlarmRepository.findByMemberId(member.getId()).stream()
                .map(AlarmDTO::from)
                .toList();
    }

    private Member getMember(String mobile) {
        return memberRepository.findByMobile(mobile).orElseThrow(
                () -> new CustomException(INVALID_USER_ID)
        );
    }

}
