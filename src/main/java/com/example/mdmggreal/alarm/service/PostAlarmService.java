package com.example.mdmggreal.alarm.service;

import com.example.mdmggreal.alarm.entity.PostAlarm;
import com.example.mdmggreal.alarm.repository.PostAlarmRepository;
import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.service.MemberGetService;
import com.example.mdmggreal.post.entity.Post;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostAlarmService {
    private final PostAlarmRepository postAlarmRepository;
    private final MemberGetService memberGetService;

    @Transactional
    public void addVotedMemberAlarm(Post post, Long memberId) {
        Member votedMember = memberGetService.getMemberByIdOrThrow(memberId);
        postAlarmRepository.save(PostAlarm.ofVotedMember(votedMember, post));

    }

    @Transactional
    public void addPostedMemberAlarm(Post post) {
        Member postedMember = post.getMember();
        postAlarmRepository.save(PostAlarm.ofPostedMember(postedMember, post));
    }

    @Transactional
    public void modifyAlarm(Long memberId, Long alarmId) {
        Member member = memberGetService.getMemberByIdOrThrow(memberId);
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

}