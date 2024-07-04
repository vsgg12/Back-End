package com.example.mdmggreal.vote.controller;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.vote.dto.VoteResultResponse;
import com.example.mdmggreal.vote.dto.request.VoteAddRequest;
import com.example.mdmggreal.vote.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/post/{postId}")
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/vote")
    public ResponseEntity<BaseResponse> voteAdd(@RequestHeader(value = "Authorization") String token, @RequestBody VoteAddRequest voteAddRequest, @PathVariable Long postId) {
        Long memberId = JwtUtil.getMemberId(token);
        voteService.saveVotes(voteAddRequest, memberId, postId);
        return BaseResponse.toResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/result")
    public ResponseEntity<VoteResultResponse> getVoteResult(@RequestHeader(value = "Authorization") String token,
                                                            @PathVariable Long postId) {
        Long memberId = JwtUtil.getMemberId(token);
        VoteResultResponse response = voteService.getVoteResult(postId, memberId);
        return ResponseEntity.ok(response);
    }

}
