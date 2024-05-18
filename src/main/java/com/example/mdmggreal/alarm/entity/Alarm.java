package com.example.mdmggreal.alarm.entity;

import com.example.mdmggreal.alarm.dto.AlarmDTO;
import com.example.mdmggreal.global.entity.BaseEntity;
import com.example.mdmggreal.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Alarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarmId")
    private Long id;

    private Long memberId;
    private String alarmContents;
    private String alarmType;

    public static Alarm from(AlarmDTO alarmDTO, Member member) {
        return Alarm.builder()
                .memberId(member.getId())
                .alarmContents(alarmDTO.getAlarmContents())
                .alarmType(alarmDTO.getAlarmType())
                .build();

    }

}
