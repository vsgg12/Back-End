package com.example.mdmggreal.post.dto.response;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.post.dto.PostDTO;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@SuperBuilder
public class PostGetListResponse extends BaseResponse {

    private List<PostDTO> postDTO;

    public static PostGetListResponse from(HttpStatus status, List<PostDTO> postDTO) {
        return PostGetListResponse.builder()
                .resultCode(status.value())
                .resultMsg(status.name())
                .postDTO(postDTO)
                .build();

    }
}
