package com.example.mdmggreal.post.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.hashtag.entity.Hashtag;
import com.example.mdmggreal.hashtag.repository.HashtagRepository;
import com.example.mdmggreal.hashtag.repository.HashtagQueryRepository;
import com.example.mdmggreal.ingameinfo.dto.response.InGameInfoResponse;
import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.ingameinfo.repository.InGameInfoRepository;
import com.example.mdmggreal.member.dto.MemberDTO;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.post.dto.PostDTO;
import com.example.mdmggreal.post.dto.request.PostAddRequest;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.post.entity.type.VideoType;
import com.example.mdmggreal.post.repository.PostRepository;
import com.example.mdmggreal.post.repository.PostQueryRepository;
import com.example.mdmggreal.posthashtag.entity.PostHashtag;
import com.example.mdmggreal.posthashtag.repository.PostHashtagRepository;
import com.example.mdmggreal.s3.service.S3Service;
import com.example.mdmggreal.vote.repository.VoteQueryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final InGameInfoRepository inGameInfoRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final HashtagQueryRepository hashtagQueryRepository;
    private final MemberRepository memberRepository;
    private final PostQueryRepository postQueryRepository;
    private final VoteQueryRepository voteQueryRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional
    public void addPost(MultipartFile uploadVideos, MultipartFile thumbnailImage, PostAddRequest postAddRequest, String content, String mobile) throws IOException {
        Member member = getMember(mobile);

        String thumbnailUrl = s3Service.uploadImages(thumbnailImage);
        String videoUrl = postAddRequest.type() == VideoType.FILE ? s3Service.uploadVideo(uploadVideos) : postAddRequest.videoUrl();

        Post post = postRepository.save(Post.of(postAddRequest, thumbnailUrl, videoUrl, content, member));

        postAddRequest.inGameInfoRequests().forEach(inGameInfo -> inGameInfoRepository.save(InGameInfo.of(inGameInfo, post)));
        postAddRequest.hashtag().forEach(name -> {
            Hashtag hashtag = hashtagRepository.findByName(name).orElseThrow(
                    () -> new CustomException(ErrorCode.HASHTAGNAME_NOT_MATCH)
            );
            postHashtagRepository.save(PostHashtag.of(post, hashtag));
        });
    }

    @Transactional
    public PostDTO getPost(Long postId, String token) {
        Post post = getPostById(postId);
        post.addView();
        return createPostDTO(post, token);
    }

    public List<PostDTO> getPostsOrderByCreatedDateTime(String token, String orderBy, String keyword) {
        List<Post> posts = postQueryRepository.getPostList(orderBy, keyword);
        return posts.stream().map(post -> createPostDTO(post, token)).collect(Collectors.toList());
    }

    public List<PostDTO> getPostsByMember(String mobile) {
        Member loginMember = getMember(mobile);
        List<Post> posts = postQueryRepository.getPostsMember(loginMember.getId());
        return posts.stream().map(post -> createPostDTO(post, loginMember.getMobile())).collect(Collectors.toList());
    }

    private PostDTO createPostDTO(Post post, String token) {
        boolean isVote = false;
        if (token != null) {
            String mobile = JwtUtil.getMobile(token);
            Member loginMember = getMember(mobile);
            isVote = voteQueryRepository.existsVoteByMemberId(post.getId(), loginMember.getId());
        }
        List<Hashtag> hashtags = hashtagQueryRepository.getListHashtagByPostId(post.getId());
        List<InGameInfoResponse> inGameInfoResponses = inGameInfoRepository.findByPostId(post.getId()).stream().map(InGameInfoResponse::of).collect(Collectors.toList());
        return PostDTO.of(MemberDTO.from(post.getMember()), post, hashtags, inGameInfoResponses, isVote);
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_POST)
        );
    }

    private Member getMember(String mobile) {
        return memberRepository.findByMobile(mobile).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_USER_ID)
        );
    }
}
