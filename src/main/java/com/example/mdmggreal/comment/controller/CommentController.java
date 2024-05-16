package com.example.mdmggreal.comment.controller;

import com.example.mdmggreal.comment.dto.CommentDTO;
import com.example.mdmggreal.comment.dto.request.CommentAddRequest;
import com.example.mdmggreal.comment.dto.response.CommentGetListResponse;
import com.example.mdmggreal.comment.service.CommentService;
import com.example.mdmggreal.global.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/post/{postid}/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<BaseResponse> commentAdd(HttpServletRequest servletRequest,
                                                   @PathVariable(value = "postid") Long postId, @RequestBody CommentAddRequest request
    ) {
        String token = (String) servletRequest.getSession().getAttribute("token");
        commentService.addComment(postId, request, token);
        return ResponseEntity.ok(BaseResponse.from(HttpStatus.CREATED));

    }

    @GetMapping
    public ResponseEntity<CommentGetListResponse> commentGetList(HttpServletRequest servletRequest, @PathVariable(value = "id") Long postId) {
        String token = (String) servletRequest.getSession().getAttribute("token");
        List<CommentDTO> commentList = commentService.getCommentList(postId, token);
        return ResponseEntity.ok(CommentGetListResponse.from(commentList, HttpStatus.OK));
    }

    @DeleteMapping("/{commentid}")
    public ResponseEntity<BaseResponse> commentDelete(HttpServletRequest servletRequest, @PathVariable(value = "postid") Long postId, @PathVariable(value = "comment") Long commentId) {
        String token = (String) servletRequest.getSession().getAttribute("token");
        commentService.deleteCommentList(postId, token, commentId);
        return ResponseEntity.ok(BaseResponse.from(HttpStatus.OK));
    }
}
