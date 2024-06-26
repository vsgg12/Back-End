package com.example.mdmggreal.vote.controller;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.vote.dto.VoteAvgDTO;
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

    @GetMapping("/users")
    public ResponseEntity<List<Post>> getVotedPostsByMemberId(@RequestHeader(value = "Authorization") String token) {
        Long memberId = JwtUtil.getMemberId(token);
        List<Post> votedPosts = voteService.getVotedPostsByMemberId(memberId);
        return ResponseEntity.ok(votedPosts);
    }

    @GetMapping("/avg")
    public ResponseEntity<List<VoteAvgDTO>> getChampionAverages(@PathVariable Long postId) {
        List<VoteAvgDTO> averageVotes = voteService.getChampionNamesWithAverageRatioByPostId(postId);
        return ResponseEntity.ok(averageVotes);
    }

}
