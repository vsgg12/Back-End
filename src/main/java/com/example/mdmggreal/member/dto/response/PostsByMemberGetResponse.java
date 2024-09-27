package com.example.mdmggreal.member.dto.response;

import com.example.mdmggreal.common.dto.PageInfo;
import com.example.mdmggreal.post.entity.type.PostStatus;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Builder
public class PostsByMemberGetResponse {

    private PageInfo pageInfo;
    private List<MyPost> postList; // 내가 쓴 글 목록

    @Getter
    public static class MyPost {

        private final Long id; //글의 id

        private final String title; //제목

        private final Long commentNum; //댓글 수

        private final String createdDate; // 작성일. 형식: yyyyMMdd

        private final String voteStatus; //판결 진행 상태

        @Builder
        public MyPost(Long id, String title, Long commentNum, LocalDateTime createdDateTime, PostStatus voteStatus) {
            this.id = id;
            this.title = title;
            this.commentNum = commentNum;
            this.createdDate = createdDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            this.voteStatus = voteStatus.getValue();
        }
    }

    public static PostsByMemberGetResponse of(Page<PostsByMemberGetResponse.MyPost> postList) {
        return PostsByMemberGetResponse.builder()
                .pageInfo(PageInfo.from(
                                postList.getSize()
                                , postList.getPageable().getPageNumber() + 1L
                                , postList.getTotalPages()
                        )
                )
                .postList(postList.getContent())
                .build();
    }
}
