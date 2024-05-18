package com.example.mdmggreal.alarm.service;

import com.example.mdmggreal.alarm.dto.AlarmDTO;
import com.example.mdmggreal.alarm.entity.Alarm;
import com.example.mdmggreal.alarm.repository.AlarmRepository;
import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    public void saveAlarm(AlarmDTO alarmDTO, String mobile) {
        Member member = getMember(mobile);
        alarmRepository.save(Alarm.from(alarmDTO, member));
    }

    public List<Alarm> getAlarmListByMemberId(String mobile) {
        Member member = getMember(mobile);
        return alarmRepository.findByMemberId(member.getId());
    }

    private Member getMember(String mobile) {
        return memberRepository.findByMobile(mobile).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_USER_ID)
        );
    }

}
