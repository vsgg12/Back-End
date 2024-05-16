package com.example.mdmggreal.post.entity;

import com.example.mdmggreal.global.entity.BaseEntity;
import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.post.dto.request.PostAddRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private Video video;
    private String title;
    private String content;
    private String thumbnailURL;
    private Long viewCount;

    @OneToMany(mappedBy = "post")
    private List<InGameInfo> inGameInfos;

    public static Post of(PostAddRequest request, String thumbnailURL, String videoUrl, Member member) {
        return Post.builder()
                .member(member)
                .title(request.title())
                .content(request.content())
                .thumbnailURL(thumbnailURL)
                .video(Video.of(videoUrl, request.type()))
                .viewCount(0L)
                .build();
    }


    public void addView() {
        this.viewCount = this.viewCount + 1;
    }


}
