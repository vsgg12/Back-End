package com.example.mdmggreal.member.dto.response;

import com.example.mdmggreal.common.dto.PageInfo;
import com.example.mdmggreal.post.dto.vo.VotedPostVo;
import lombok.Builder;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
public record VotedPostsByMemberGetResponse(
        PageInfo pageInfo, // 페이지 관련 정보
        List<VotedPost> postList
) {
    @Builder
    public record VotedPost(
            Long id, // 글의 id
            String title, // 글의 제목
            String authorNickname, // 글 작성자 닉네임
            String authorTier, // 글 작성자 티어
            String voteStatus, // 판결 진행 상태
            String myVoteResult, // 내 판결 결과. 판결 진행 중인 경우 null 반환
            String createdDate // 게시글 작성일. 형식-yyyyMMdd
    ) {
        public static VotedPost from(VotedPostVo post, String myVoteResult) {
            return VotedPost.builder()
                    .id(post.getPostId())
                    .title(post.getTitle())
                    .authorNickname(post.getAuthorNickname())
                    .authorTier(post.getAuthorTier().getName())
                    .voteStatus(post.getStatus().getValue())
                    .myVoteResult(myVoteResult)
                    .createdDate(post.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .build();
        }
    }

    public static VotedPostsByMemberGetResponse from(PageInfo pageInfo, List<VotedPost> postList) {
        return VotedPostsByMemberGetResponse.builder()
                .pageInfo(pageInfo)
                .postList(postList)
                .build();
    }
}
