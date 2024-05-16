package com.example.mdmggreal.vote.controller;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.vote.dto.VoteDTO;
import com.example.mdmggreal.vote.service.VoteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/vote")
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/save")
    public ResponseEntity<BaseResponse> save(@RequestBody List<VoteDTO> voteDTOs, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("token");
        voteService.saveVotes(voteDTOs, token);
        return ResponseEntity.ok(BaseResponse.from(HttpStatus.OK));

    }

}
