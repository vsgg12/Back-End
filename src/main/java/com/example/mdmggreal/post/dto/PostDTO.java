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
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
    private Long daysUntilEnd; // 게시글 마감까지 남은 일자
    private List<Hashtag> hashtagList;
    private List<InGameInfoDTO> inGameInfoList;
    private Boolean isVote;
    private BooleanEnum isDeleted; // 게시글 삭제 여부

    public static PostDTO of(MemberDTO memberDTO, Post post, List<Hashtag> hashtagList, List<InGameInfoDTO> inGameInfoList, Boolean isVote) {
        // 마감까지 남은 일자 기준: 마감일 당일 조회 시 - 0일
        // 24-11-11T23:59:59.9999 의 경우 11월 12일로 계산되기 때문에, 날짜만 추출한 후, 시간은 0시0분으로 따로 넣음.
        LocalDateTime now = LocalDateTime.now();
        long daysUntilEnd = ChronoUnit.DAYS.between(now, LocalDateTime.of(post.getEndDateTime().toLocalDate(), LocalTime.of(0, 0)));

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
                .daysUntilEnd(daysUntilEnd)
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
