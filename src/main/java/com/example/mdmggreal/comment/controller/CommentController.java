package com.example.mdmggreal.comment.controller;

import com.example.mdmggreal.comment.dto.request.CommentAddRequest;
import com.example.mdmggreal.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/post/{id}/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> commentAdd(HttpServletRequest servletRequest,
                                        @PathVariable(value = "id") Long postId, @RequestBody CommentAddRequest request
    ) {
        String token = (String) servletRequest.getSession().getAttribute("token");
        commentService.addComment(postId, request, token);
        return ResponseEntity.ok().build();

    }
}
