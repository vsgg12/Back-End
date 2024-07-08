package com.example.mdmggreal.ingameinfo.dto.response;

import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class InGameInfoDTO {

    private Long inGameInfoId;
    private String tier;
    private String position;
    private String championName;
    private Double averageRatio;

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
