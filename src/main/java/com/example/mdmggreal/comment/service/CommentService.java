package com.example.mdmggreal.comment.service;

import com.example.mdmggreal.alarm.service.CommentAlarmService;
import com.example.mdmggreal.comment.dto.CommentDTO;
import com.example.mdmggreal.comment.dto.request.CommentAddRequest;
import com.example.mdmggreal.comment.entity.Comment;
import com.example.mdmggreal.comment.repository.CommentDAO;
import com.example.mdmggreal.comment.repository.CommentRepository;
import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mdmggreal.global.exception.ErrorCode.INVALID_COMMENT;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentDAO commentDAO;
    private final CommentAlarmService commentAlarmService;

    @Transactional
    public void addComment(Long postId, CommentAddRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_USER_ID)
        );
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_POST)
        );
        Comment child = null;

        if (request.getParentId() != null && !request.getParentId().equals("")) {
            Comment parent = commentRepository.findById(request.getParentId()).orElseThrow(
                    () -> new CustomException(INVALID_COMMENT)
            );
            commentAlarmService.addAlarm(parent, request);
            child = Comment.of(post, member, parent, request);
        } else {
            child = Comment.of(post, member, request);
        }
        if (child != null) {
            commentRepository.save(child);
        }
    }

    @Transactional
    public List<CommentDTO> getCommentList(Long postId) {
        List<Comment> list = commentDAO.getList(postId);
        List<CommentDTO> commentResponseDTOList = new ArrayList<>();
        Map<Long, CommentDTO> commentDTOHashMap = new HashMap<>();

        list.forEach(c -> {
            CommentDTO commentResponseDTO = CommentDTO.from(c);
            commentDTOHashMap.put(commentResponseDTO.getId(), commentResponseDTO);
            if (c.getParent() != null)
                commentDTOHashMap.get(c.getParent().getId()).getChildren().add(commentResponseDTO);
            else commentResponseDTOList.add(commentResponseDTO);
        });
        return commentResponseDTOList;
    }

    @Transactional
    public void deleteCommentList(Long postId, Long memberId, Long commentId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_USER_ID)
        );
        Comment comment = commentDAO.findCommentByIdWithParent(commentId)
                .orElseThrow(() -> new CustomException(INVALID_COMMENT));
        if (!comment.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }
        if (comment.getChildren().size() != 0) {
            comment.changeIsDeleted(true);
        } else {
            commentRepository.delete(getDeletableAncestorComment(comment));
        }
    }

    private Comment getDeletableAncestorComment(Comment comment) {
        Comment parent = comment.getParent();
        if (parent != null && parent.getChildren().size() == 1 && parent.getIsDeleted())

            return getDeletableAncestorComment(parent);
        return comment;
    }
}
