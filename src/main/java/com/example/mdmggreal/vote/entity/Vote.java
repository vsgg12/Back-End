package com.example.mdmggreal.vote.entity;

import com.example.mdmggreal.global.entity.BaseEntity;
import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Vote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long memberId;

    @ManyToOne
    @JoinColumn(name="ingame_info_id")
    private InGameInfo inGameInfo;

    private Long ratio;


}
