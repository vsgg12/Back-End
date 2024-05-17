package com.example.mdmggreal.vote.controller;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.vote.dto.VoteAvgDTO;
import com.example.mdmggreal.vote.dto.VoteSaveDTO;
import com.example.mdmggreal.vote.service.VoteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/vote")
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/save")
    public ResponseEntity<BaseResponse> save(@RequestHeader(value = "Authorization") String token, @RequestBody List<VoteSaveDTO> voteDTOs, HttpServletRequest request) {
        JwtUtil.validateToken(token);
        String mobile = JwtUtil.getMobile(token);
        voteService.saveVotes(voteDTOs, mobile);
        return ResponseEntity.ok(BaseResponse.from(HttpStatus.OK));
    }

    @GetMapping("/users")
    public ResponseEntity<List<Post>> getVotedPostsByMemberId(@RequestHeader(value = "Authorization") String token, HttpServletRequest request) {
        JwtUtil.validateToken(token);
        String mobile = JwtUtil.getMobile(token);
        List<Post> votedPosts = voteService.getVotedPostsByMemberId(mobile);
        return ResponseEntity.ok(votedPosts);
    }

    @GetMapping("/{postId}/avg")
    public ResponseEntity<List<VoteAvgDTO>> getChampionAverages(@PathVariable Long postId) {
        List<VoteAvgDTO> averageVotes = voteService.getChampionNamesWithAverageRatioByPostId(postId);
        return ResponseEntity.ok(averageVotes);
    }

}
