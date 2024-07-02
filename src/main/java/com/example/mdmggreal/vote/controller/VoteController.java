package com.example.mdmggreal.vote.controller;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.vote.dto.VoteResultResponse;
import com.example.mdmggreal.vote.dto.VoteSaveDTO;
import com.example.mdmggreal.vote.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/post/{postId}")
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/vote")
    public ResponseEntity<BaseResponse> save(@RequestHeader(value = "Authorization") String token, @RequestBody List<VoteSaveDTO> voteDTOs, @PathVariable Long postId) {
        Long memberId = JwtUtil.getMemberId(token);
        voteService.saveVotes(voteDTOs, memberId, postId);
        return ResponseEntity.ok(BaseResponse.from(HttpStatus.OK));
    }

    @GetMapping("/result")
    public ResponseEntity<VoteResultResponse> getVoteResult(@RequestHeader(value = "Authorization") String token,
                                                            @PathVariable Long postId) {
        Long memberId = JwtUtil.getMemberId(token);
        VoteResultResponse response = voteService.getVoteResult(postId, memberId);
        return ResponseEntity.ok(response);
    }

}
