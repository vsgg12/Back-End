package com.example.mdmggreal.post.dto.response;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.ingameinfo.dto.response.InGameInfoResponse;
import com.example.mdmggreal.post.dto.PostDTO;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@SuperBuilder
public class PostGetResponse extends BaseResponse {

    private PostDTO postDTO;
    private List<InGameInfoResponse> inGameInfo;

    public static PostGetResponse from(HttpStatus status, PostDTO postDTO, List<InGameInfoResponse> inGameInfo) {
        return PostGetResponse.builder()
                .resultCode(status.value())
                .resultMsg(status.name())
                .postDTO(postDTO)
                .inGameInfo(inGameInfo)
                .build();

    }
}
