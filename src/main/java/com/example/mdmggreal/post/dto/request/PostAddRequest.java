package com.example.mdmggreal.post.dto.request;


import com.example.mdmggreal.ingameinfo.dto.request.InGameInfoRequest;
import com.example.mdmggreal.post.entity.type.VideoType;

import java.util.List;
public record PostAddRequest(
        String title,
//        String content,
        VideoType type,
        List<String> hashtag,
        List<InGameInfoRequest> inGameInfoRequests,
        String videoUrl
) {






}
