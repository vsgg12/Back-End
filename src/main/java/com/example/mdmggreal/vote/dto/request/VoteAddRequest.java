package com.example.mdmggreal.vote.dto.request;

import com.example.mdmggreal.vote.dto.VoteDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class VoteAddRequest {
    private List<VoteDTO> voteList;
}
