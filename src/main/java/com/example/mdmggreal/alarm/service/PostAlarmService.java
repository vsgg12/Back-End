package com.example.mdmggreal.alarm.service;

import com.example.mdmggreal.alarm.dto.AlarmDTO;
import com.example.mdmggreal.alarm.entity.PostAlarm;
import com.example.mdmggreal.alarm.repository.PostAlarmRepository;
import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void addAlarm(Post post, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_USER_ID)
        );
        postAlarmRepository.save(PostAlarm.from(member, post));

    }

    public void modifyAlarm(String mobile, Long alarmId) {
        Member member = getMember(mobile);
        PostAlarm postAlarm = postAlarmRepository.findById(alarmId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_ALARM)
        );
        validatePermission(member, postAlarm);
        postAlarm.editIsRead();
    }

    private void validatePermission(Member member, PostAlarm postAlarm) {
        if (!postAlarm.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }
    }

    private Member getMember(String mobile) {
        return memberRepository.findByMobile(mobile).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_USER_ID)
        );
    }
}