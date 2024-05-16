package com.example.mdmggreal.post.dto.response;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.post.dto.PostDTO;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@SuperBuilder
public class PostGetResponse extends BaseResponse {

    private PostDTO postDTO;

    public static PostGetResponse from(HttpStatus status, PostDTO postDTO) {
        return PostGetResponse.builder()
                .resultCode(status.value())
                .resultMsg(status.name())
                .postDTO(postDTO)
                .build();

    }
}
