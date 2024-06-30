package com.example.mdmggreal.alarm.service;

import com.example.mdmggreal.alarm.dto.AlarmDTO;
import com.example.mdmggreal.alarm.repository.CommentAlarmRepository;
import com.example.mdmggreal.alarm.repository.PostAlarmRepository;
import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
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

    public List<AlarmDTO> getAlarmList(Long memberId) {
        Member member = getMemberByMemberId(memberId);
        List<AlarmDTO> alarmDTOList = new ArrayList<>();

        alarmDTOList.addAll(postAlarmRepository.findByMemberId(member.getId())
                .stream().map(AlarmDTO::from)
                .toList());

        alarmDTOList.addAll(commentAlarmRepository.findByMemberId(member.getId())
                .stream().map(AlarmDTO::from)
                .toList());

        return alarmDTOList.stream()
                .sorted(Comparator  // 정렬 1순위 : 안 읽은 순, 2순위 : 최근 순
                        .comparing(AlarmDTO::getIsRead)
                        .thenComparing(AlarmDTO::getCreatedDateTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    private Member getMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(INVALID_USER_ID)
        );
    }
}
