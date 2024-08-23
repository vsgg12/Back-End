package com.example.mdmggreal.member.service;

import com.example.mdmggreal.member.dto.response.PostsByMemberGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MyPageService {

    @Transactional(readOnly = true)
    public PostsByMemberGetResponse getPostsByMemberId(Long memberId) {
        List<PostsByMemberGetResponse.MyPost> postList = new ArrayList<>();
        postList.add(PostsByMemberGetResponse.MyPost.builder().build());

        return PostsByMemberGetResponse.builder()
                .postList(postList)
                .build();
    }
}
