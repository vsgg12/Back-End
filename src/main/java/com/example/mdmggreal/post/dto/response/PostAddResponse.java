package com.example.mdmggreal.post.dto.response;

import com.example.mdmggreal.global.response.BaseResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@SuperBuilder
public class PostAddResponse extends BaseResponse {

    public static PostAddResponse of(HttpStatus httpStatus) {
        return PostAddResponse.builder()
                .resultCode(httpStatus.value())
                .resultMsg(httpStatus.name())
                .build();
    }
}
