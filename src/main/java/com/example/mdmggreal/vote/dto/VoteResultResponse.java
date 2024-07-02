package com.example.mdmggreal.vote.dto;

import com.example.mdmggreal.global.response.BaseResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.List;

@SuperBuilder
@Getter
public class VoteResultResponse extends BaseResponse {

    private Long postId;
    private List<InGameInfoResult> results;

    @Getter
    @Builder
    public static class InGameInfoResult {
        private String championName;
        private Double votedRatio;
        private String position; // 한글
        private String tier; // 한글
    }

    public static VoteResultResponse from(Long postId, List<InGameInfoResult> results, HttpStatus httpStatus){
        return VoteResultResponse.builder()
                .resultCode(httpStatus.value())
                .resultMsg(httpStatus.name())
                .postId(postId)
                .results(results)
                .build();
    }
}
