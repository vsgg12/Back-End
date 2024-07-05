package com.example.mdmggreal.vote.controller;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.vote.dto.VoteResultResponse;
import com.example.mdmggreal.vote.dto.request.VoteAddRequest;
import com.example.mdmggreal.vote.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/api/post/{postId}")
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/vote")
    public ResponseEntity<BaseResponse> votesAdd(@RequestHeader(value = "Authorization") String token,
                                             @RequestBody VoteAddRequest request,
                                             @PathVariable Long postId) {
        Long memberId = JwtUtil.getMemberId(token);
        voteService.addVotes(request, memberId, postId);
        return BaseResponse.toResponseEntity(OK);
    }

    @GetMapping("/result")
    public ResponseEntity<VoteResultResponse> getVoteResult(@RequestHeader(value = "Authorization") String token,
                                                            @PathVariable Long postId) {
        Long memberId = JwtUtil.getMemberId(token);
        VoteResultResponse response = voteService.getVoteResult(postId, memberId);
        return ResponseEntity.ok(response);
    }

}
