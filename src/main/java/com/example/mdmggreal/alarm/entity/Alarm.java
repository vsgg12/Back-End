package com.example.mdmggreal.alarm.entity;

import com.example.mdmggreal.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Alarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarmId")
    private Long id;

    private String token;
    private String alarmContents;
    private String alarmType;

}
