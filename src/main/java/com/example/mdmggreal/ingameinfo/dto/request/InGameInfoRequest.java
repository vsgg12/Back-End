package com.example.mdmggreal.ingameinfo.dto.request;

import com.example.mdmggreal.ingameinfo.type.Position;
import com.example.mdmggreal.ingameinfo.type.Tier;

public record InGameInfoRequest(
        String championName,
        Position position,
        Tier tier) {

}
