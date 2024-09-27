package com.example.mdmggreal.ingameinfo.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.mdmggreal.ingameinfo.dto.response.QInGameInfoDTO is a Querydsl Projection type for InGameInfoDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QInGameInfoDTO extends ConstructorExpression<InGameInfoDTO> {

    private static final long serialVersionUID = -2034193254L;

    public QInGameInfoDTO(com.querydsl.core.types.Expression<Long> inGameInfoId, com.querydsl.core.types.Expression<com.example.mdmggreal.ingameinfo.type.InGameTier> tier, com.querydsl.core.types.Expression<com.example.mdmggreal.ingameinfo.type.Position> position, com.querydsl.core.types.Expression<String> championName, com.querydsl.core.types.Expression<Double> averageRatio) {
        super(InGameInfoDTO.class, new Class<?>[]{long.class, com.example.mdmggreal.ingameinfo.type.InGameTier.class, com.example.mdmggreal.ingameinfo.type.Position.class, String.class, double.class}, inGameInfoId, tier, position, championName, averageRatio);
    }

}

