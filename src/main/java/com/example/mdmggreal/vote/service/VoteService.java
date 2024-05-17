package com.example.mdmggreal.vote.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final VoteRepository voteRepository;

    public List<Vote> saveVotes(List<VoteDTO> voteDTOs, String mobile) {
        Member member = getMember(mobile);
        List<Vote> votes = voteDTOs.stream()
                .map(voteDTO -> convertToEntity(voteDTO, mobile))
                .collect(Collectors.toList());
        return voteRepository.saveAll(votes);
    }



    public Vote convertToEntity(VoteDTO voteDTO, String mobile) {
        Member member = getMember(mobile);
        InGameInfo inGameInfo = new InGameInfo(voteDTO.getIngameInfoId());
        return Vote.builder()
                .ratio(voteDTO.getRatio())
                .memberId(member.getId())
                .inGameInfo(inGameInfo)
                .build();
    }
    private Member getMember(String mobile) {
        return memberRepository.findByMobile(mobile).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_USER_ID)
        );
    }
}
