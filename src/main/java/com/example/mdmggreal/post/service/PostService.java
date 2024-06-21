package com.example.mdmggreal.post.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.hashtag.entity.Hashtag;
import com.example.mdmggreal.hashtag.repository.HashtagRepository;
import com.example.mdmggreal.hashtag.repository.HashtagRepositoryImpl;
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
import com.example.mdmggreal.post.repository.PostRepositoryImpl;
import com.example.mdmggreal.posthashtag.entity.PostHashtag;
import com.example.mdmggreal.posthashtag.repository.PostHashtagRepository;
import com.example.mdmggreal.s3.service.S3Service;
import com.example.mdmggreal.vote.repository.VoteRepositoryImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final InGameInfoRepository inGameInfoRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final HashtagRepositoryImpl hashtagRepositoryImpl;
    private final MemberRepository memberRepository;
    private final PostRepositoryImpl postRepositoryImpl;
    private final VoteRepositoryImpl voteRepositoryImpl;
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
        Member member = post.getMember();
        post.addView();

        List<Hashtag> hashtags = hashtagRepositoryImpl.getListHashtagByPostId(post.getId());
        List<InGameInfoResponse> inGameInfoResponses = inGameInfoRepository.findByPostId(postId).stream().map(InGameInfoResponse::of).toList();
        boolean isVote = false;
        if (token != null) {
            String mobile = JwtUtil.getMobile(token);
            Member loginMember = getMember(mobile);
            isVote = voteRepositoryImpl.existsVoteByMemberId(post.getId(), loginMember.getId());
        }

        return PostDTO.of(MemberDTO.from(member), post, hashtags, inGameInfoResponses, isVote);
    }

    public List<PostDTO> getPostsOrderByCreatedDateTime(String token, String orderBy, String keyword) {
        List<Post> posts = postRepositoryImpl.getPostList(orderBy, keyword);
        List<PostDTO> postDTOS = new ArrayList<>();


        posts.forEach(post -> {
            MemberDTO member = MemberDTO.from(post.getMember());
            List<Hashtag> listHashtagByPostId = hashtagRepositoryImpl.getListHashtagByPostId(post.getId());
            boolean isVote = false;
            List<InGameInfoResponse> inGameInfoResponses = inGameInfoRepository.findByPostId(post.getId()).stream()
                    .map(InGameInfoResponse::of).toList();

            if (token != null) {
                String mobile = JwtUtil.getMobile(token);
                Member loginMember = getMember(mobile);
                isVote = voteRepositoryImpl.existsVoteByMemberId(post.getId(), loginMember.getId());
            }

            postDTOS.add(PostDTO.of(member, post, listHashtagByPostId, inGameInfoResponses, isVote));
        });

        return postDTOS;
    }

    public List<PostDTO> getPostsByMember(String mobile) {
        Member loginMember = getMember(mobile);
        List<Post> posts = postRepositoryImpl.getPostsMember(loginMember.getId());
        List<PostDTO> postDTOS = new ArrayList<>();

        posts.forEach(post -> {
            List<InGameInfoResponse> inGameInfoResponses = inGameInfoRepository.findByPostId(post.getId()).stream().map(InGameInfoResponse::of).toList();
            MemberDTO member = MemberDTO.from(post.getMember());
            List<Hashtag> listHashtagByPostId = hashtagRepositoryImpl.getListHashtagByPostId(post.getId());
            boolean isVote = voteRepositoryImpl.existsVoteByMemberId(post.getId(), loginMember.getId());
            postDTOS.add(PostDTO.of(member, post, listHashtagByPostId, inGameInfoResponses, isVote));
        });

        return postDTOS;
    }

    public List<PostDTO> getPostsKeyword(String token, String keyWord) {
        List<Post> posts = postRepositoryImpl.getPostsKeyword(keyWord);
        List<PostDTO> postDTOS = new ArrayList<>();

        posts.forEach(post -> {
            MemberDTO from = MemberDTO.from(post.getMember());
            List<Hashtag> listHashtagByPostId = hashtagRepositoryImpl.getListHashtagByPostId(post.getId());
            boolean isVote = false;
            List<InGameInfoResponse> inGameInfoResponses = inGameInfoRepository.findByPostId(post.getId()).stream().map(InGameInfoResponse::of).toList();

            if (token != null) {

                String mobile = JwtUtil.getMobile(token);
                Member loginMember = getMember(mobile);
                isVote = voteRepositoryImpl.existsVoteByMemberId(post.getId(), loginMember.getId());
            }

            postDTOS.add(PostDTO.of(from, post, listHashtagByPostId, inGameInfoResponses, isVote));
        });

        return postDTOS;
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
