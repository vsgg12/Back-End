package com.example.mdmggreal.vote.service;

import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.service.MemberService;
import com.example.mdmggreal.vote.dto.VoteDTO;
import com.example.mdmggreal.vote.entity.Vote;
import com.example.mdmggreal.vote.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VoteService {

    private final MemberService memberService;

    private final VoteRepository voteRepository;

    public List<Vote> saveVotes(List<VoteDTO> voteDTOs, String token) {
        List<Vote> votes = voteDTOs.stream()
                .map(voteDTO -> convertToEntity(voteDTO, token))
                .collect(Collectors.toList());
        return voteRepository.saveAll(votes);
    }

    public Vote convertToEntity(VoteDTO voteDTO, String token) {
        Member memberByToken = memberService.getMemberByToken(token);
        return Vote.builder()
                .ratio(voteDTO.getRatio())
                .memberId(memberByToken.getId())
                .build();
    }
}
