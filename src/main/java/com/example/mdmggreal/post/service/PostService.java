package com.example.mdmggreal.post.service;

import com.example.mdmggreal.global.entity.type.BooleanEnum;
import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.hashtag.entity.Hashtag;
import com.example.mdmggreal.hashtag.repository.HashtagQueryRepository;
import com.example.mdmggreal.hashtag.repository.HashtagRepository;
import com.example.mdmggreal.ingameinfo.dto.response.InGameInfoDTO;
import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.ingameinfo.repository.InGameInfoRepository;
import com.example.mdmggreal.member.dto.MemberDTO;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.post.dto.PostDTO;
import com.example.mdmggreal.post.dto.request.PostAddRequest;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.post.entity.type.VideoType;
import com.example.mdmggreal.post.repository.PostQueryRepository;
import com.example.mdmggreal.post.repository.PostRepository;
import com.example.mdmggreal.posthashtag.entity.PostHashtag;
import com.example.mdmggreal.posthashtag.repository.PostHashtagRepository;
import com.example.mdmggreal.s3.service.S3Service;
import com.example.mdmggreal.vote.repository.VoteQueryRepository;
import com.example.mdmggreal.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.example.mdmggreal.global.exception.ErrorCode.INVALID_USER_ID;
import static com.example.mdmggreal.global.exception.ErrorCode.NO_PERMISSION_TO_DELETE_POST;

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
    private final VoteRepository voteRepository;

    @Transactional
    public void addPost(MultipartFile videoFile, MultipartFile thumbnailImage, PostAddRequest postAddRequest, String content, Long memberId) throws IOException {
        Member member = getMemberByMemberId(memberId);
        String thumbnailUrl;
        if (thumbnailImage == null || thumbnailImage.isEmpty()) {
            thumbnailUrl = createThumbnailImageFromVideo(videoFile);
        } else {
            thumbnailUrl = s3Service.uploadImages(thumbnailImage);
        }

        String videoUrl = postAddRequest.videoType() == VideoType.FILE ? s3Service.uploadVideo(videoFile) : postAddRequest.videoLink();

        Post post = postRepository.save(Post.of(postAddRequest, thumbnailUrl, videoUrl, content, member));

        postAddRequest.inGameInfoRequests().forEach(inGameInfo -> inGameInfoRepository.save(InGameInfo.of(inGameInfo, post)));
        postAddRequest.hashtag().forEach(name -> {
            if (hashtagRepository.findByName(name).isPresent()) {
                Hashtag hashtag = hashtagRepository.findByName(name).get();
                postHashtagRepository.save(PostHashtag.of(post, hashtag));
            } else {
                Hashtag hashtag = Hashtag.from(name);
                Hashtag savedHashtag = hashtagRepository.save(hashtag);
                postHashtagRepository.save(PostHashtag.of(post, savedHashtag));
            }

        });

        rewardPoint(member);
    }


    @Transactional
    public PostDTO getPost(Long postId, Long memberId) {
        Post post = getPostById(postId);
        post.addView();
        return createPostDTO(post, memberId);
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getPostsOrderByCreatedDateTime(Long memberId, String orderBy, String keyword) {
        List<Post> posts = postQueryRepository.getPostList(orderBy, keyword);
        return posts.stream().map(post -> createPostDTO(post, memberId)).toList();
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getPostsByMember(Long memberId) {
        Member loginMember = getMemberByMemberId(memberId);
        List<Post> posts = postQueryRepository.getPostsMember(loginMember.getId());
        return posts.stream().map(post -> createPostDTO(post, loginMember.getId())).toList();
    }

    @Transactional
    public void deletePost(Long postId, Long memberId) {
        Member loginMember = getMemberByMemberId(memberId);
        Post post = getPostById(postId);
        if (!post.getMember().getId().equals(loginMember.getId())) {
            throw new CustomException(NO_PERMISSION_TO_DELETE_POST);
        }
        post.deleted();
    }

    /*
    todo 비디오 파일을 이용한 썸네일 추출기능
     */
    private String createThumbnailImageFromVideo(MultipartFile videoFile) {
        return null;
    }

    private PostDTO createPostDTO(Post post, Long memberId) {
        boolean isVote = false;
        if (memberId != null) {
            Member loginMember = getMemberByMemberId(memberId);
            isVote = voteQueryRepository.existsVoteByMemberId(post.getId(), loginMember.getId());
        }
        List<Hashtag> hashtags = hashtagQueryRepository.getListHashtagByPostId(post.getId());
        List<InGameInfoDTO> inGameInfoRespons = inGameInfoRepository.findByPostId(post.getId()).stream().map(InGameInfoDTO::of).toList();
        return PostDTO.of(MemberDTO.from(post.getMember()), post, hashtags, inGameInfoRespons, isVote);
    }

    private Post getPostById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_POST)
        );
        if (post.getIsDeleted().equals(BooleanEnum.TRUE)) {
            throw new CustomException(ErrorCode.INVALID_POST);
        }
        return post;
    }

    private Member getMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(INVALID_USER_ID)
        );
    }

    private void rewardPoint(Member member) {
        member.rewardPointByPostCreation(member.getTier().getPostCreationPoint());
    }

}
