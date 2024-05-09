package com.example.mdmggreal.image.dto.response;

import com.example.mdmggreal.global.response.BaseResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@SuperBuilder
public class ImageUploadResponse extends BaseResponse {
    private List<String> images;

    public static ImageUploadResponse from(List<String> images, HttpStatus status) {
        return ImageUploadResponse.builder()
                .resultCode(status.value())
                .resultMsg(status.name())
                .images(images)
                .build();
    }
}
