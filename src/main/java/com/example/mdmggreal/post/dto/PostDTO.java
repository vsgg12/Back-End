package com.example.mdmggreal.post.dto;

import com.example.mdmggreal.member.dto.MemberDTO;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.post.entity.Video;
import com.example.mdmggreal.post.entity.type.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private String thumbnailURL;
    private Long viewCount;
    private PostStatus status;
    private Video video;
    private MemberDTO memberDTO;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostDTO of(MemberDTO memberDTO, Post post) {
        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .thumbnailURL(post.getThumbnailURL())
                .video(post.getVideo())
                .status(post.getStatus())
                .viewCount(post.getViewCount())
                .memberDTO(memberDTO)
                .createdAt(post.getCreatedDateTime())
                .updatedAt(post.getModifyDateTime())
                .build();

    }
}
