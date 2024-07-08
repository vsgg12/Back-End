package com.example.mdmggreal.vote.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class VoteAddRequest {

    private List<VoteAddDTO> voteList;

    @Getter
    public static class VoteAddDTO {

        private Long inGameInfoId;
        private Integer ratio;
    }
}
