package com.example.mdmggreal.alarm.service;

import com.example.mdmggreal.alarm.dto.AlarmDTO;
import com.example.mdmggreal.alarm.entity.PostAlarm;
import com.example.mdmggreal.alarm.repository.PostAlarmRepository;
import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.mdmggreal.global.exception.ErrorCode.INVALID_USER_ID;

@Service
@RequiredArgsConstructor
public class PostAlarmService {
    private final PostAlarmRepository postAlarmRepository;
    private final MemberRepository memberRepository;

    public List<AlarmDTO> getPostAlarmList(String mobile) {
        Member member = getMember(mobile);
        return postAlarmRepository.findByMemberId(member.getId()).stream()
                .map(AlarmDTO::from)
                .toList();
    }

    public void modifyAlarm(String mobile, Long alarmId) {
        Member member = getMember(mobile);
        PostAlarm postAlarm = postAlarmRepository.findById(alarmId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_ALARM)
        );
        if (!postAlarm.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }

        postAlarm.editIsRead();
    }

    private Member getMember(String mobile) {
        return memberRepository.findByMobile(mobile).orElseThrow(
                () -> new CustomException(INVALID_USER_ID)
        );
    }


}
