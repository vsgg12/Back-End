package com.example.mdmggreal.comment.service;

import com.example.mdmggreal.alarm.service.CommentAlarmService;
import com.example.mdmggreal.comment.dto.request.CommentAddRequest;
import com.example.mdmggreal.comment.dto.response.CommentGetListResponse;
import com.example.mdmggreal.comment.entity.Comment;
import com.example.mdmggreal.comment.repository.CommentQueryRepository;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.mdmggreal.global.exception.ErrorCode.COMMENT_NOT_EXISTS;
import static com.example.mdmggreal.global.exception.ErrorCode.INVALID_COMMENT;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentQueryRepository commentQueryRepository;
    private final CommentAlarmService commentAlarmService;
    private final MemberGetService memberGetService;

    @Transactional
    public void addComment(Long postId, CommentAddRequest request, Long memberId) {
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

    /**
     * 게시글의 댓글 목록 조회
     */
    @Transactional
    public List<CommentGetListResponse.CommentDTO> getCommentList(Long postId) {
        List<Comment> list = commentQueryRepository.getList(postId);
        Map<Long, CommentGetListResponse.CommentDTO> dtoMap = new LinkedHashMap<>(); // Map<댓글 ID, 댓글 DTO>
        Map<Long, CommentGetListResponse.CommentDTO> resultMap = new LinkedHashMap<>(); // Map<댓글 ID, 댓글 DTO>

        list.forEach(comment -> {
            // DTO 생성
            CommentGetListResponse.CommentDTO commentDTO = CommentGetListResponse.CommentDTO.from(comment);
            dtoMap.put(comment.getId(), commentDTO);

            // 최상위 댓글만 결과 맵에 저장
            if (comment.getParent() == null) {
                resultMap.put(comment.getId(), commentDTO);
            }
        });

        list.forEach(comment -> {
            if (comment.getParent() != null) {
                // 최상위 부모 댓글을 찾아서 그 댓글의 자식 리스트에 추가
                CommentGetListResponse.CommentDTO rootParent = findRootParent(comment.getParent(), dtoMap);
                if (rootParent != null) {
                    rootParent.getChildren().add(dtoMap.get(comment.getId()));
                }
            }
        });

        return resultMap.values().stream().toList();
    }

    /*
    최상위 댓글 찾는 메서드
     */
    private CommentGetListResponse.CommentDTO findRootParent(Comment parent, Map<Long, CommentGetListResponse.CommentDTO> dtoMap) {
        while (parent.getParent() != null) {
            parent = parent.getParent(); // 부모의 부모로 계속 올라감
        }
        return dtoMap.get(parent.getId());
    }

    /**
     * 댓글 삭제
     * - 대댓글이 달린 댓글은 삭제 불가
     */
    @Transactional
    public void deleteComment(Long memberId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(COMMENT_NOT_EXISTS));

        if (!memberId.equals(comment.getMember().getId())) {
            throw new CustomException(ErrorCode.NO_PERMISSION_TO_DELETE_COMMENT);
        }

        if (!commentRepository.findByParentId(commentId).isEmpty()) {
            throw new CustomException(ErrorCode.CANNOT_DELETE_COMMENT_WITH_CHILD);
        }

        comment.changeIsDeleted(true);
    }

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
