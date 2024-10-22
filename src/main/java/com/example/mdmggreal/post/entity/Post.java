package com.example.mdmggreal.post.entity;

import com.example.mdmggreal.global.entity.BaseEntity;
import com.example.mdmggreal.global.entity.type.BooleanEnum;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.post.dto.request.PostAddRequest;
import com.example.mdmggreal.post.entity.type.PostStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.example.mdmggreal.global.entity.type.BooleanEnum.FALSE;
import static com.example.mdmggreal.global.entity.type.BooleanEnum.TRUE;
import static com.example.mdmggreal.post.entity.type.PostStatus.FINISHED;
import static com.example.mdmggreal.post.entity.type.PostStatus.PROGRESS;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private Video video;

    @Enumerated(STRING)
    private PostStatus status;

    private String title;

    private String content;

    private String thumbnailURL;

    private Long viewCount;

    private LocalDateTime endDateTime;

    @Enumerated(STRING)
    private BooleanEnum isDeleted;

    public static Post of(PostAddRequest request, LocalDateTime endDateTime, String thumbnailURL, String videoUrl, String content, Member member) {
        return Post.builder()
                .member(member)
                .title(request.title())
                .content(content)
                .thumbnailURL(thumbnailURL)
                .status(PROGRESS)
                .video(Video.of(videoUrl, request.videoType()))
                .viewCount(0L)
                .endDateTime(endDateTime)
                .isDeleted(FALSE)
                .build();
    }

    public void editStatus() {
        this.status = FINISHED;
    }

    public void addView() {
        this.viewCount = this.viewCount + 1;
    }

    public void deleteThumbnail() {
        this.thumbnailURL = null;
    }

    public void deleteVideo() {
        this.video = null;
    }

    public void delete() {
        this.isDeleted = TRUE;
    }
}
