package com.example.mdmggreal.post.dto;

import com.example.mdmggreal.global.entity.type.BooleanEnum;
import com.example.mdmggreal.hashtag.entity.Hashtag;
import com.example.mdmggreal.ingameinfo.dto.response.InGameInfoDTO;
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
    private List<InGameInfoDTO> inGameInfoList;
    private Boolean isVote;
    private BooleanEnum isDeleted; // 게시글 삭제 여부

    public static PostDTO of(MemberDTO memberDTO, Post post, List<Hashtag> hashtagList, List<InGameInfoDTO> inGameInfoList, Boolean isVote) {

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
                .isDeleted(post.getIsDeleted())
                .build();

    }

    /*
    삭제된 게시글 조회 시 사용
     */
    public static PostDTO createDeletedPostDTO(Long postId) {
        return PostDTO.builder()
                .id(postId)
                .isDeleted(BooleanEnum.TRUE)
                .build();
    }
}
