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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.mdmggreal.global.exception.ErrorCode.*;
import static java.math.BigDecimal.ZERO;

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

        LocalDateTime requestEndDateTime = validateAndConvertStringToDateTime(postAddRequest.voteEndDate());

        String thumbnailUrl;
        thumbnailUrl = s3Service.uploadImages(thumbnailImage);

        String videoUrl = postAddRequest.videoType() == VideoType.FILE ? s3Service.uploadVideo(videoFile) : postAddRequest.videoLink();

        Post post = postRepository.save(Post.of(postAddRequest, requestEndDateTime, thumbnailUrl, videoUrl, content, member));

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
    }

    @Transactional
    public void rewardPointByPostCreation(Member member) {
        member.increasePoint(member.getMemberTier().getPostCreationPoint());
    }

    @Transactional
    public PostDTO getPost(Long postId, Long memberId) {
        Post post = getPostById(postId);

        if (post.getIsDeleted().equals(BooleanEnum.TRUE)) { // 삭제된 게시글의 경우
            return PostDTO.createDeletedPostDTO(postId);
        }

        post.addView();
        return createPostDTO(post, memberId);
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getPostsOrderByCreatedDateTime(Long memberId, String orderBy, String keyword) {
        List<Post> posts = postQueryRepository.getPostList(orderBy, keyword);
        return posts.stream().map(post -> createPostDTO(post, memberId)).toList();
    }

    @Transactional
    public void deletePost(Long postId, Long memberId) {
        Member loginMember = getMemberByMemberId(memberId);
        Post post = getPostById(postId);

        if (!post.getMember().getId().equals(loginMember.getId())) {
            throw new CustomException(NO_PERMISSION_TO_DELETE_POST);
        }

        deleteThumbnailAndVideo(post);
        post.delete();
    }

    /*
    썸네일 이미지, 게시글 동영상 삭제
     */
    private void deleteThumbnailAndVideo(Post post) {
        // S3에서 썸네일이미지, 게시글동영상 삭제
        String thumbnailURL = post.getThumbnailURL();
        String videoURL = post.getVideo().getUrl();
        List<String> deleteUrls = List.of(thumbnailURL, videoURL);
        s3Service.deleteS3Objects(deleteUrls);

        // db 썸네일, 동영상 삭제 처리
        post.deleteThumbnail();
        post.deleteVideo();
    }

    /**
     * 판결 종료일은 오늘부터 최소 1일 후, 최대 30일 후의 날짜로 설정 가능
     */
    private LocalDateTime validateAndConvertStringToDateTime(String voteEndDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime requestEndDateTime = LocalDate.parse(voteEndDate, formatter)
                .atTime(LocalTime.of(23, 59, 59, 999_999_000));

        long daysBetween = ChronoUnit.DAYS.between(LocalDateTime.now(), requestEndDateTime);
        if (daysBetween >= 1 && daysBetween <= 30) {
            return requestEndDateTime;
        } else {
            throw new CustomException(INVALID_END_DATE);
        }
    }

    private PostDTO createPostDTO(Post post, Long memberId) {
        boolean isVote = false;
        if (memberId != null) {
            Member loginMember = getMemberByMemberId(memberId);
            isVote = voteQueryRepository.existsVoteByMemberId(post.getId(), loginMember.getId());
        }

        List<Hashtag> hashtags = hashtagQueryRepository.getListHashtagByPostId(post.getId());

        List<InGameInfoDTO> inGameInfoDTOList = createInGameInfoDTOList(post.getId());

        return PostDTO.of(MemberDTO.from(post.getMember()), post, hashtags, inGameInfoDTOList, isVote);
    }

    /**
     * 판결 결과 표시 로직
     */
    public List<InGameInfoDTO> createInGameInfoDTOList(Long postId) {
        List<InGameInfo> inGameInfoList = inGameInfoRepository.findByPostId(postId);
        BigDecimal votedMembersCount = new BigDecimal(voteRepository.countByInGameInfoId(inGameInfoList.get(0).getId()).toString());

        // 투표자 없을 경우 전부 0.0으로 표시
        if (votedMembersCount.compareTo(ZERO) == 0) {
            return inGameInfoList.stream().map(inGameInfo ->
                    InGameInfoDTO.of(inGameInfo, 0.0)).toList();
        }

        // 투표자 있는 경우 소수점 둘째 자리까지 자른 후 표시
        HashMap<Long, BigDecimal> inGameInfoAverageRatioMap = new HashMap<>();
        inGameInfoList.forEach(inGameInfo ->
                inGameInfoAverageRatioMap.put(inGameInfo.getId(),
                        new BigDecimal(inGameInfo.getTotalRatio()).divide(votedMembersCount, 2, RoundingMode.DOWN))
        );

        return inGameInfoList.stream().map(inGameInfo ->
                InGameInfoDTO.of(inGameInfo, inGameInfoAverageRatioMap.get(inGameInfo.getId()).doubleValue())).toList();
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
}
