package com.example.mdmggreal.post.dto;

import com.example.mdmggreal.hashtag.entity.Hashtag;
import com.example.mdmggreal.ingameinfo.dto.response.InGameInfoResponse;
import com.example.mdmggreal.member.dto.MemberDTO;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.post.entity.Video;
import com.example.mdmggreal.post.entity.type.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<Hashtag> hashtagList;
    private List<InGameInfoResponse> inGameInfoList;
    private Boolean isVote;

    public static PostDTO of(MemberDTO memberDTO, Post post, List<Hashtag> hashtagList, List<InGameInfoResponse> inGameInfoList, Boolean isVote) {

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
                .hashtagList(hashtagList)
                .inGameInfoList(inGameInfoList)
                .isVote(isVote)
                .build();

    }
}
