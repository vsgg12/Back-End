package com.example.mdmggreal.comment.controller;

import com.example.mdmggreal.comment.dto.CommentDTO;
import com.example.mdmggreal.comment.dto.request.CommentAddRequest;
import com.example.mdmggreal.comment.dto.response.CommentGetListResponse;
import com.example.mdmggreal.comment.service.CommentService;
import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/post/{postid}/comment")
@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;


    @PostMapping
    public ResponseEntity<BaseResponse> commentAdd(@RequestHeader(value = "Authorization") String token,
                                                   @PathVariable(value = "postid") Long postId, @RequestBody CommentAddRequest request
    ) {
        JwtUtil.validateToken(token);
        String mobile = JwtUtil.getMobile(token);
        commentService.addComment(postId, request, mobile);
        return ResponseEntity.ok(BaseResponse.from(HttpStatus.CREATED));

    }

    @GetMapping
    public ResponseEntity<CommentGetListResponse> commentGetList(@RequestHeader(value = "Authorization") String token, @PathVariable(value = "postid") Long postId) {
        JwtUtil.validateToken(token);
        String mobile = JwtUtil.getMobile(token);
        List<CommentDTO> commentList = commentService.getCommentList(postId, mobile);
        return ResponseEntity.ok(CommentGetListResponse.from(commentList, HttpStatus.OK));
    }

    @DeleteMapping("/{commentid}")
    public ResponseEntity<BaseResponse> commentDelete(@RequestHeader(value = "Authorization") String token, @PathVariable(value = "postid") Long postId, @PathVariable(value = "comment") Long commentId) {
        JwtUtil.validateToken(token);
        String mobile = JwtUtil.getMobile(token);
        commentService.deleteCommentList(postId, mobile, commentId);
        return ResponseEntity.ok(BaseResponse.from(HttpStatus.OK));
    }

}
