package com.example.mdmggreal.post.controller;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.post.dto.PostDTO;
import com.example.mdmggreal.post.dto.request.PostAddRequest;
import com.example.mdmggreal.post.dto.response.PostAddResponse;
import com.example.mdmggreal.post.dto.response.PostGetListResponse;
import com.example.mdmggreal.post.dto.response.PostGetResponse;
import com.example.mdmggreal.post.entity.type.VideoType;
import com.example.mdmggreal.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.example.mdmggreal.global.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostAddResponse> postAdd(@RequestHeader(value = "Authorization") String token,
                                                   @RequestPart("uploadVideos") MultipartFile videoFile,
                                                   @RequestPart("content") MultipartFile contentFile,
                                                   @RequestPart("thumbnailImage") MultipartFile thumbnailImage,
                                                   @RequestPart("postAddRequest") @Valid PostAddRequest postAddRequest
    ) throws IOException {
        Long memberId = JwtUtil.getMemberId(token);
        checkVideoAttachment(videoFile, postAddRequest);
        String content = new String(contentFile.getBytes(), StandardCharsets.UTF_8);

        postService.addPost(videoFile, thumbnailImage, postAddRequest, content, memberId);

        return ResponseEntity.ok(PostAddResponse.of(HttpStatus.CREATED));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostGetResponse> postGet(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long postId) {
        Long memberId = null;
        if (token != null && !token.isEmpty()) {
            memberId = JwtUtil.getMemberId(token);
        }
        PostDTO post = postService.getPost(postId, memberId);
        return ResponseEntity.ok(PostGetResponse.from(OK, post));
    }

    @GetMapping
    public ResponseEntity<PostGetListResponse> postsGetOrderByCreatedDateTime(@RequestHeader(value = "Authorization", required = false) String token, @RequestParam("orderby") String orderBy, @RequestParam("keyword") String keyWord) {
        Long memberId = null;
        if (token != null && !token.isEmpty()) {
            memberId = JwtUtil.getMemberId(token);
        }
        List<PostDTO> posts = postService.getPostsOrderByCreatedDateTime(memberId, orderBy, keyWord);
        return ResponseEntity.ok(PostGetListResponse.from(OK, posts));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<BaseResponse> postDelete(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long postId) {
        Long memberId = JwtUtil.getMemberId(token);
        postService.deletePost(postId, memberId);
        return BaseResponse.toResponseEntity(OK);
    }

    private void checkVideoAttachment(MultipartFile videoFile, PostAddRequest postAddRequest) {
        if ((videoFile == null || videoFile.isEmpty()) && (postAddRequest.videoLink() == null || postAddRequest.videoLink().isEmpty())) {
            throw new CustomException(VIDEO_REQUIRED);
        }
        if (postAddRequest.videoType().equals(VideoType.LINK) && postAddRequest.videoLink() == null) {
            throw new CustomException(VIDEO_LINK_REQUIRED);
        }
        if (postAddRequest.videoType().equals(VideoType.FILE) && videoFile == null) {
            throw new CustomException(VIDEO_FILE_REQUIRED);
        }
    }
}
