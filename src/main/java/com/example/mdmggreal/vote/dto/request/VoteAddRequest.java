package com.example.mdmggreal.vote.dto.request;

import com.example.mdmggreal.global.response.BaseResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class VoteAddRequest extends BaseResponse {

    private List<VoteAddDTO> voteList;

    @Getter
    @Builder
    public static class VoteAddDTO {

        private Long inGameInfoId;
        private Integer ratio;
    }
}
