package com.example.mdmggreal.ingameinfo.dto.response;

import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.ingameinfo.type.Position;
import com.example.mdmggreal.ingameinfo.type.Tier;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class InGameInfoResponse {

    private Long inGameInfoId;
    private String tier;
    private String position;
    private String championName;

    public static InGameInfoResponse of(InGameInfo inGameInfo) {
        return InGameInfoResponse.builder()
                .inGameInfoId(inGameInfo.getId())
                .tier(inGameInfo.getTier().getName())
                .position(inGameInfo.getPosition().getName())
                .championName(inGameInfo.getChampionName())
                .build();
    }

}
