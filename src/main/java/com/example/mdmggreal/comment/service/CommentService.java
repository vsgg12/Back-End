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
import com.example.mdmggreal.member.service.MemberGetService;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mdmggreal.global.exception.ErrorCode.INVALID_COMMENT;
import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentDAO commentDAO;
    private final CommentAlarmService commentAlarmService;
    private final MemberGetService memberGetService;

    @Transactional
    public void addComment(CommentAddRequest request, Long memberId) {
        Long postId = request.getPostId();
        Member commentedMember = memberGetService.getMemberByIdOrThrow(memberId);

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_POST)
        );
        Comment addedComment;

        // 댓글을 db에 저장하기 전에 point 지급을 먼저 해야함.
        // 저장 후에 지급하면, 이미 그 게시글에 댓글을 작성한 것으로 판별되어 포인트 지급이 안됨.
        rewardPoint(postId, commentedMember);

        if (request.getParentId() == null) { // 댓글 작성
            addedComment = Comment.of(post, commentedMember, request);

            if (!memberId.equals(post.getMember().getId())) { // 댓글 작성자와 글 작성자가 다른 경우만 알람 생성
                commentAlarmService.addCommentAlarm(addedComment, commentedMember.getNickname());
            }
        } else { // 대댓글 작성
            Comment parentComment = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new CustomException(INVALID_COMMENT));

            if (!post.getId().equals(parentComment.getPost().getId())) {
                throw new CustomException(ErrorCode.INVALID_PARENT_ID);
            }

            addedComment = Comment.of(post, commentedMember, parentComment, request);

            if (!memberId.equals(parentComment.getMember().getId())) { // 대댓글 작성자와 부모댓글 작성자가 다른 경우만 알람 생성
                commentAlarmService.addChildCommentAlarm(addedComment, parentComment, commentedMember.getNickname());
            }
        }

        if (addedComment != null) {
            commentRepository.save(addedComment);
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
                commentDTOHashMap.get(c.getParent().getId()).getChildren().add(0, commentResponseDTO); // 대댓글 오래된 순으로 정렬
            else commentResponseDTOList.add(commentResponseDTO);
        });
        return commentResponseDTOList;
    }

    @Transactional
    public void deleteComment(Long memberId, Long commentId, Long postId) {
        Member member = memberGetService.getMemberByIdOrThrow(memberId);
        Comment comment = commentDAO.findCommentByIdWithParent(commentId)
                .orElseThrow(() -> new CustomException(INVALID_COMMENT));
        if (!comment.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.NO_PERMISSION_TO_DELETE_COMMENT);
        }
        comment.changeIsDeleted(TRUE);
        /*
        todo 1.댓글 삭제시 포인트 롤백정책 정해질 경우 로직구현
         */
//        validateRollbackPoint(postId, memberId);
    }

//    private void validateRollbackPoint(Long postId, Long memberId) {
//        boolean isCommentAuthor = commentRepository.existsByPostIdAndMemberId(postId, memberId);
//        if (!isCommentAuthor) {
//            rollbackPoint(postId, memberId);
//        }
//    }
//
//    private void rollbackPoint(Long postId, Long memberId) {
//
//    }

    private void rewardPoint(Long postId, Member commentedMember) {
        Long commentedMemberId = commentedMember.getId();
        LocalDate today = LocalDate.now();

        boolean isAlreadyCommentOnPost = commentRepository.existsByPostIdAndMemberId(postId, commentedMemberId);
        long todayRewardedCommentCount = commentRepository.countDistinctPostsWithCommentsByMemberAndDate(
                commentedMemberId, today.atStartOfDay(), today.atTime(LocalTime.MAX)
        );

        if (!isAlreadyCommentOnPost && todayRewardedCommentCount < 30) {
            commentedMember.increasePoint(5);
        }
    }
}
