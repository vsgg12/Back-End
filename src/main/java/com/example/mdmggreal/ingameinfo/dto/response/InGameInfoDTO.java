package com.example.mdmggreal.ingameinfo.dto.response;

import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.ingameinfo.type.InGameTier;
import com.example.mdmggreal.ingameinfo.type.Position;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class InGameInfoDTO {

    private Long inGameInfoId;
    private String tier;
    private String position;
    private String championName;
    private Double averageRatio;

    @QueryProjection
    public InGameInfoDTO(Long inGameInfoId, InGameTier tier, Position position, String championName, Double averageRatio) {
        this.inGameInfoId = inGameInfoId;
        this.tier = tier.getName();
        this.position = position.getName();
        this.championName = championName;
        this.averageRatio = averageRatio;
    }



    public static InGameInfoDTO of(InGameInfo inGameInfo, Double averageRatio) {
        return InGameInfoDTO.builder()
                .inGameInfoId(inGameInfo.getId())
                .tier(inGameInfo.getInGameTier().getName())
                .position(inGameInfo.getPosition().getName())
                .championName(inGameInfo.getChampionName())
                .averageRatio(averageRatio)
                .build();
    }

}
