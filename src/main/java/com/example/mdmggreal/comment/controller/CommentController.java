package com.example.mdmggreal.comment.controller;

import com.example.mdmggreal.comment.dto.CommentDTO;
import com.example.mdmggreal.comment.dto.request.CommentAddRequest;
import com.example.mdmggreal.comment.dto.response.CommentGetListResponse;
import com.example.mdmggreal.comment.service.CommentService;
import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.global.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

//@RequestMapping("/api/comment")
@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;


    @PostMapping("/api/post/{postId}/comment")
    public ResponseEntity<BaseResponse> commentAdd(@RequestHeader(value = "Authorization") String token,
                                                   @PathVariable(value = "postId") Long postId,
                                                   @RequestBody @Valid CommentAddRequest request
    ) {
        Long memberId = JwtUtil.getMemberId(token);
        commentService.addComment(postId, request, memberId);
        return BaseResponse.toResponseEntity(CREATED);
    }

    @GetMapping("/api/post/{postId}/comment")
    public ResponseEntity<CommentGetListResponse> commentGetList(@PathVariable(value = "postId") Long postId) {
        List<CommentDTO> commentList = commentService.getCommentList(postId);
        return ResponseEntity.ok(CommentGetListResponse.from(commentList, OK));
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/api/comment/{commentId}")
    public ResponseEntity<BaseResponse> commentDelete(@RequestHeader(value = "Authorization") String token,
                                                      @PathVariable(value = "commentId") Long commentId) {
        Long memberId = JwtUtil.getMemberId(token);
        commentService.deleteComment(memberId, commentId);
        return BaseResponse.toResponseEntity(OK);
    }

}
