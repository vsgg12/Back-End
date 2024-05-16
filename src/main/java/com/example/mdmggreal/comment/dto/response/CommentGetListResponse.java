package com.example.mdmggreal.comment.dto.response;

import com.example.mdmggreal.comment.dto.CommentDTO;
import com.example.mdmggreal.global.response.BaseResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@SuperBuilder
public class CommentGetListResponse extends BaseResponse {
    private List<CommentDTO> comments;

    public static CommentGetListResponse from(List<CommentDTO> comments, HttpStatus status) {
        return CommentGetListResponse.builder()
                .resultCode(status.value())
                .resultMsg(status.name())
                .comments(comments)
                .build();
    }
}
