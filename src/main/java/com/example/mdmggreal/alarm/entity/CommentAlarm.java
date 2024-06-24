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
public class CommentAlarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private Boolean isRead;
    private String alarmContents;

    public static CommentAlarm from(Comment comment, CommentAddRequest request) {
        return CommentAlarm.builder()
                .member(comment.getMember())
                .comment(comment)
                .alarmContents(request.getContent())
                .isRead(FALSE)
                .build();

    }

    public void editIsRead() {
        this.isRead = true;
    }

}
