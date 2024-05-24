package com.example.mdmggreal.post.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.hashtag.entity.Hashtag;
import com.example.mdmggreal.hashtag.repository.HashtagRepository;
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
import com.example.mdmggreal.service.S3Service;
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
    private final HashtagRepository hashtagRepository;
    private final MemberRepository memberRepository;
    private final PostRepositoryImpl postRepositoryImpl;

    @Transactional
    public void addPost(MultipartFile uploadVideos, MultipartFile thumbnailImage, PostAddRequest postAddRequest, String content, String mobile) throws IOException {

        Member member = getMember(mobile);

        String thumbnailUrl = s3Service.uploadImages(thumbnailImage);

        String videoUrl = null;

        if (postAddRequest.type() == VideoType.FILE) {
            videoUrl = s3Service.uploadVideo(uploadVideos);
        } else {
            videoUrl = postAddRequest.videoUrl();
        }

        Post post = postRepository.save(Post.of(postAddRequest, thumbnailUrl, videoUrl, content, member));

        postAddRequest.inGameInfoRequests().forEach(inGameInfo -> {
            inGameInfoRepository.save(InGameInfo.of(inGameInfo, post));
        });

        postAddRequest.hashtag().forEach(name -> {
            Hashtag hashtag = hashtagRepository.save(Hashtag.from(name));
            postHashtagRepository.save(PostHashtag.of(post, hashtag));
        });

    }

    @Transactional
    public PostDTO getPost(Long postId, String mobile) {
        Member member = getMember(mobile);


        Post post = getPost(postId);
        post.addView();
        return PostDTO.of(MemberDTO.from(member), post);

    }

    public List<PostDTO> getPostsOrderByCreatedDateTime(/*String mobile, */String orderBy) {
//        Member member = getMember(mobile);

        List<Post> posts = postRepositoryImpl.getPostsOrderByCreatedDateTime(orderBy);
        List<PostDTO> postDTOS = new ArrayList<>();
        posts.forEach(post -> {
            MemberDTO from = MemberDTO.from(post.getMember());
            postDTOS.add(PostDTO.of(from, post));
        });

        return postDTOS;
    }



    public List<PostDTO> getPostsByMember(String mobile) {
        Member member = getMember(mobile);

        List<Post> posts = postRepositoryImpl.getPostsMember(member.getId());
        List<PostDTO> postDTOS = new ArrayList<>();
        posts.forEach(post -> {
            MemberDTO from = MemberDTO.from(post.getMember());
            postDTOS.add(PostDTO.of(from, post));
        });

        return postDTOS;
    }

    public List<PostDTO> getPostsKeyword(String mobile, String keyWord) {
        Member member = getMember(mobile);
        List<Post> posts = postRepositoryImpl.getPostsKeyword(keyWord);
        List<PostDTO> postDTOS = new ArrayList<>();
        posts.forEach(post -> {
            MemberDTO from = MemberDTO.from(post.getMember());
            postDTOS.add(PostDTO.of(from, post));
        });

        return postDTOS;
    }


    private Post getPost(Long postId) {
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
