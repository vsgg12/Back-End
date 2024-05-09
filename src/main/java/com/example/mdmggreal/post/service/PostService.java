package com.example.mdmggreal.post.service;

import com.example.mdmggreal.amazon.service.S3Service;
import com.example.mdmggreal.hashtag.entity.Hashtag;
import com.example.mdmggreal.hashtag.repository.HashtagRepository;
import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.ingameinfo.repository.InGameInfoRepository;
import com.example.mdmggreal.post.dto.PostAddRequest;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.post.entity.Video;
import com.example.mdmggreal.post.entity.type.VideoType;
import com.example.mdmggreal.post.repository.PostRepository;
import com.example.mdmggreal.posthashtag.entity.PostHashtag;
import com.example.mdmggreal.posthashtag.repository.PostHashtagRepository;
import com.example.mdmggreal.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final InGameInfoRepository inGameInfoRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final HashtagRepository hashtagRepository;

//    @Transactional
//    public void addPost(String token, MultipartFile uploadVideos, MultipartFile thumbnailImage, PostAddRequest postAddRequest) throws IOException {
//        String thumbnailUrl = s3Service.uploadImages(thumbnailImage);
//
//        String videoUrl = null;
//
//        if (postAddRequest.type() == VideoType.FILE) {
//            videoUrl = s3Service.uploadVideo(uploadVideos);
//        } else {
//            videoUrl = postAddRequest.videoUrl();
//        }
//
//        Post post = postRepository.save(Post.of(postAddRequest, thumbnailUrl, videoUrl));
//
//        postAddRequest.inGameInfoRequests().forEach(inGameInfo -> {
//            inGameInfoRepository.save(InGameInfo.of(inGameInfo, post));
//        });
//
//        postAddRequest.hashtag().forEach(name -> {
//            Hashtag hashtag = hashtagRepository.save(Hashtag.from(name));
//            postHashtagRepository.save(PostHashtag.of(post, hashtag));
//        });
//
//    }

    @Transactional
    public void addPost(MultipartFile uploadVideos, MultipartFile thumbnailImage, PostAddRequest postAddRequest) throws IOException {
        String thumbnailUrl = s3Service.uploadImages(thumbnailImage);

        String videoUrl = null;

        if (postAddRequest.type() == VideoType.FILE) {
            videoUrl = s3Service.uploadVideo(uploadVideos);
        } else {
            videoUrl = postAddRequest.videoUrl();
        }

        Post post = postRepository.save(Post.of(postAddRequest, thumbnailUrl, videoUrl));

        postAddRequest.inGameInfoRequests().forEach(inGameInfo -> {
            inGameInfoRepository.save(InGameInfo.of(inGameInfo, post));
        });

        postAddRequest.hashtag().forEach(name -> {
            Hashtag hashtag = hashtagRepository.save(Hashtag.from(name));
            postHashtagRepository.save(PostHashtag.of(post, hashtag));
        });

    }

}
