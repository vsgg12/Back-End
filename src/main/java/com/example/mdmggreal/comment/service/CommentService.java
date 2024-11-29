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

    @Transactional
    public List<CommentGetListResponse.CommentDTO> getCommentList(Long postId) {
        List<Comment> list = commentQueryRepository.getList(postId);
        Map<Long, CommentGetListResponse.CommentDTO> resultMap = new LinkedHashMap<>(); // Map<댓글 ID, 댓글 DTO>
        Map<Long, LinkedHashMap<Long, CommentGetListResponse.CommentDTO>> commentChildMap = new LinkedHashMap<>(); // Map<댓글 ID, LinkedHashMap<댓글 하위의 답글 ID, 답글 DTO>>

        // 원래 구조
//        list.forEach(c -> {
//            Comment commentResponseDTO = Comment.from(c);
//            commentDTOHashMap.put(commentResponseDTO.getId(), commentResponseDTO);
//            if (c.getParent() != null)
//                commentDTOHashMap.get(c.getParent().getId()).getChildren().add(0, commentResponseDTO); // 대댓글 오래된 순으로 정렬
//            else commentResponseDTOList.add(commentResponseDTO);
//        });
//
        // 댓글은 최신 순으로 정렬, 대댓글은 작성 순으로 정렬
        // 1. 짱구 : 배고프다 - id=1, parentId=null, grandParentId=null
        // 2. 나나 : @짱구 어쩌라고? - id=2, parentId=1, grandParentId=1
        // 3. 철구 : @나나 인성 무엇 - id=3, parentId=2, grandParentId=1
        // 4. 슬기 : @짱구 물 마시셈 - id=4, parentId=1, grandParentId=1
        // 5. 철구 : @짱구 나도.... - id=5, parentId=1, grandParentId=1
        // 6. 나나 : @철구 반사 ㅋㅋ - id=6, parentId=3, grandParentId=1

        // 3번에서, 철구가 나나한테 댓글을 단 경우 댓글 알림이 어떻게 생성되나?
        // 현재는 A)내가 쓴 게시글에 댓글이 달렸을 때, B)내가 쓴 댓글에 대댓글이 달렸을 때 댓글 알림 발송.
        // 3번 케이스의 경우 A는 아님(우리 정책 상 댓글과 대댓글은 다름).
        // 그럼 B인가? 짱구에게 대댓글을 남긴게 아니라 짱구의 대댓글에 대댓글을 남길 때도 짱구한테 댓글 알림이 가나?
        // "나나님이 글에 답글을 남겼습니다. ~~~~" 이렇게?
        // 그럼 만약 짱구의 댓글에서 대댓글들이 막 싸우고 있으면 짱구한테 계속 알람이 가는 건가??
        // 나나와 짱구 둘 다 알림이 가는지, 나나만 가는지, 짱구만 가는지...

        list.forEach(comment -> {
            CommentGetListResponse.CommentDTO commentDTO = CommentGetListResponse.CommentDTO.from(comment);

            // 일반 댓글인 경우
            if (comment.getParent() == null) {
                resultMap.put(comment.getId(), commentDTO);
            }

            // 답글/자식답글인 경우
            if (comment.getParent() != null) {
                CommentGetListResponse.CommentDTO parentCommentInResult = resultMap.get(comment.getParent().getId());

                // 1. 답글인 경우 - parentId 댓글의 childMap에 추가
                if (parentCommentInResult != null) {
                    if (commentChildMap.get(comment.getParent().getId()).isEmpty()) {
                        LinkedHashMap<Long, List<CommentGetListResponse.CommentDTO>> emptyChildMap = new LinkedHashMap<>();
                        emptyChildMap.put(commentDTO.getId(), List.of(commentDTO));
//                        commentChildMap.put(comment.getParent().getId(), emptyChildMap);
                    }
                }

                // 2. 자식답글인 경우 - childMap에 parentId 댓글이 존재하면 그 childMap에 넣기


                // 3. childMap을 한번에 resultMap에 맞는 곳에 넣는다.


//                // 1. 답글인 경우 - parentId 댓글의 childMap에 추가
//                if (resultComment != null) {
//                    resultComment.getChildren().add(commentDTO);
//                }
//
//                // 2. 자식답글인 경우 - parentId 댓글이 childMap에 있는 경우, childMap에 추가
//                if (resultComment == null) {
//                    resultMap.values().forEach(commentDTO1 -> {
//                        if (commentDTO1.getChildren().contains(parentCommentDTO)) {
//                            resultMap.get(commentDTO1.getId()).getChildren().add(commentDTO);
//                        }
//                    });
//                }

                // 1. 답글인 경우 - parentId
            }
        });
        return resultMap.values().stream().toList();
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
