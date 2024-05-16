package com.example.mdmggreal.post.service;

import com.example.mdmggreal.ingameinfo.dto.request.InGameInfoRequest;
import com.example.mdmggreal.ingameinfo.type.Position;
import com.example.mdmggreal.ingameinfo.type.Tier;
import com.example.mdmggreal.member.dto.MemberDTO;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.service.MemberService;
import com.example.mdmggreal.post.dto.request.PostAddRequest;
import com.example.mdmggreal.post.entity.type.VideoType;
import com.example.mdmggreal.post.repository.PostRepository;
import com.example.mdmggreal.posthashtag.repository.PostHashtagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    PostRepository postRepository;
    @Mock
    PostHashtagRepository postHashtagRepository;
    @Mock
    MemberService memberService;
    @Mock
    com.example.mdmggreal.amazon.service.S3Service s3Service;

    @InjectMocks
    PostService postService;

    private Member testMember;

    @BeforeEach
    void setUp() {
        // 테스트용 Member 객체 생성
        MemberDTO memberDTO = MemberDTO.builder()
                .nickname("testUser")
                .age("20")
                .email("test@example.com")
                .mobile("010-1234-5678")
                .gender("M")
                .build();
        testMember = Member.from(memberDTO);
    }

    @Test
    @DisplayName("게시물 추가 테스트")
    void addPostTest() throws Exception {
        // given
        String token = "testToken";
        MockMultipartFile thumbnailImage = new MockMultipartFile("thumbnailImage", new byte[]{});
        MockMultipartFile uploadVideo = new MockMultipartFile("uploadVideo", new byte[]{});
        List<InGameInfoRequest> inGameInfoRequests = List.of(
                new InGameInfoRequest("가렌", Position.TOP, Tier.DIAMOND),
                new InGameInfoRequest("워윅", Position.JUNGLE, Tier.DIAMOND)
        );

        List<String> hashtags = List.of("협곡", "랭크");
        PostAddRequest postAddRequest = new PostAddRequest(
                "테스트제목", "테스트내용", VideoType.LINK, hashtags, inGameInfoRequests, "etstURl"
        );

        // memberService.getMemberByToken() 메서드가 호출될 때 testMember 반환하도록 설정
        when(memberService.getMemberByToken(token)).thenReturn(testMember);

        // when
        postService.addPost(uploadVideo, thumbnailImage, postAddRequest, token);

        // then
        // postRepository.save() 등 각 메서드가 호출되었는지 확인
        verify(postRepository, times(1)).save(any());
        verify(postHashtagRepository, times(1)).save(any());
    }
}
