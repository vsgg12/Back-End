package com.example.mdmggreal.comment.service;

import com.example.mdmggreal.comment.dto.request.CommentAddRequest;
import com.example.mdmggreal.comment.entity.Comment;
import com.example.mdmggreal.comment.repository.CommentRepository;
import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.service.MemberService;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final PostRepository postRepository;


    @Transactional
    public void addComment(Long postId, CommentAddRequest request, String token) {
        Member member = memberService.getMemberByToken(token);
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_POST)
        );
        Comment child = null;

        if (request.getParentId() != null && !request.getParentId().equals("")) {
            Comment parent = commentRepository.findById(request.getParentId()).orElseThrow(
                    () -> new CustomException(ErrorCode.INVALID_COMMENT)
            );
            child = Comment.of(post, member, parent, request);
        } else {
            child = Comment.of(post, member, request);
        }
        if (child != null) {
            commentRepository.save(child);
        }
    }
}
