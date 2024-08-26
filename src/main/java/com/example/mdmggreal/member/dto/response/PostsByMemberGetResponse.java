package com.example.mdmggreal.member.dto.response;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.post.entity.type.PostStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@SuperBuilder
public class PostsByMemberGetResponse extends BaseResponse {
    /**
     * 내가 쓴 글 목록
     */
    private List<MyPost> postList;

    @Getter
    public static class MyPost {
        /**
         * 글의 id
         */
        private Long id;

        /**
         * 제목
         */
        private String title;

        /**
         * 댓글 수
         */
        private Long commentNum;

        /**
         * 작성일
         * - yyyyMMdd
         */
        private String createdDate;

        /**
         * 판결 진행 상태
         */
        private String voteStatus;

        public MyPost(Long id, String title, Long commentNum, LocalDateTime createdDateTime, PostStatus voteStatus) {
            this.id = id;
            this.title = title;
            this.commentNum = commentNum;
            this.createdDate = createdDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            this.voteStatus = voteStatus.getValue();
        }
    }
}
