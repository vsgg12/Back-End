package com.example.mdmggreal.vote.service;

import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.service.MemberService;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.vote.dto.VoteSaveDTO;
import com.example.mdmggreal.vote.dto.VoteStatisticsDTO;
import com.example.mdmggreal.vote.entity.Vote;
import com.example.mdmggreal.vote.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VoteService {

    private final MemberService memberService;
    private final VoteRepository voteRepository;

    public List<VoteStatisticsDTO> getChampionNamesWithAverageRatioByPostId(Long postId) {
        return voteRepository.findChampionNamesWithAverageRatioByPostId(postId);
    }

    public List<Post> getVotedPostsByMemberId(String token) {
        Member memberByToken = memberService.getMemberByToken(token);
        List<Vote> votes = voteRepository.findByMemberId(memberByToken.getId());
        return votes.stream()
                .map(Vote::getInGameInfo)
                .filter(Objects::nonNull)
                .map(InGameInfo::getPost)
                .collect(Collectors.toList());
    }

    public List<Vote> saveVotes(List<VoteSaveDTO> voteSaveDTOS, String token) {
        List<Vote> votes = voteSaveDTOS.stream()
                .map(voteSaveDTO -> convertToEntity(voteSaveDTO, token))
                .collect(Collectors.toList());
        return voteRepository.saveAll(votes);
    }

    public Vote convertToEntity(VoteSaveDTO voteSaveDTO, String token) {
        Member memberByToken = memberService.getMemberByToken(token);
        InGameInfo inGameInfo = new InGameInfo(voteSaveDTO.getIngameInfoId());

        return Vote.builder()
                .ratio(voteSaveDTO.getRatio())
                .memberId(memberByToken.getId())
                .inGameInfo(inGameInfo)
                .build();
    }
}
