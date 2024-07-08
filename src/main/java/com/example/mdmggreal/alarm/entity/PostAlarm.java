package com.example.mdmggreal.alarm.entity;

import com.example.mdmggreal.global.entity.BaseEntity;
import com.example.mdmggreal.global.entity.type.BooleanEnum;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.post.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.mdmggreal.global.entity.type.BooleanEnum.FALSE;
import static com.example.mdmggreal.global.entity.type.BooleanEnum.TRUE;
import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostAlarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(STRING)
    private BooleanEnum isRead;

    private String alarmContents;

    public static PostAlarm ofVotedMember(Member votedMember, Post post) {
        return PostAlarm.builder()
                .member(votedMember)
                .alarmContents(votedMember.getNickname() + "님이 참여한 게시글의 판결 결과를 확인하세요!")
                .post(post)
                .isRead(FALSE)
                .build();
    }

    public static PostAlarm ofPostedMember(Member postedMember, Post post) {
        return PostAlarm.builder()
                .member(postedMember)
                .alarmContents(postedMember.getNickname() + "님이 작성한 게시글의 판결 결과를 확인하세요!")
                .post(post)
                .isRead(FALSE)
                .build();
    }

    public void editIsRead() {
        this.isRead = TRUE;
    }
}
