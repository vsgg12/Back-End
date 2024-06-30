package com.example.mdmggreal.alarm.entity;

import com.example.mdmggreal.comment.dto.request.CommentAddRequest;
import com.example.mdmggreal.comment.entity.Comment;
import com.example.mdmggreal.global.entity.BaseEntity;
import com.example.mdmggreal.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.lang.Boolean.FALSE;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
// 댓글 알람 종류
// 1. 사용자의 게시글에 댓글이 달리는 경우
// 2. 사용자의 댓글에 대댓글이 달리는 경우
public class CommentAlarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    // 알람을 받는 사용자
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // 달린 댓글/대댓글
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    // 알림 읽음 여부
    private Boolean isRead;

    // 알림 내용
    private String alarmContents;

    public static CommentAlarm from(Comment comment, String commentedNickname, Member alarmedMember) {
        StringBuilder alarmContents = new StringBuilder();
        alarmContents.append(commentedNickname);
        if (comment.getParent() == null) {
            alarmContents.append("님이 글에 댓글을 남겼습니다.");
        } else {;
            alarmContents.append("님이 글에 답글을 남겼습니다.");
        }

        return CommentAlarm.builder()
                .member(alarmedMember)
                .comment(comment)
                .isRead(FALSE)
                .alarmContents(alarmContents.toString())
                .build();
    }

    public void editIsRead() {
        this.isRead = true;
    }

}
