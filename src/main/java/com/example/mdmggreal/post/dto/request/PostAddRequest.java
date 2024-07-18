package com.example.mdmggreal.post.dto.request;


import com.example.mdmggreal.ingameinfo.dto.request.InGameInfoRequest;
import com.example.mdmggreal.post.entity.type.VideoType;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PostAddRequest(
        String title,
        VideoType videoType,
        List<@Size(max = 12, message = "해시태그는 공백포함 12자까지 작성가능합니다.") String> hashtag,
        List<InGameInfoRequest> inGameInfoRequests,
        String videoLink
) {


}
