package com.example.mdmggreal.post.controller;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.post.dto.PostDTO;
import com.example.mdmggreal.post.dto.request.PostAddRequest;
import com.example.mdmggreal.post.dto.response.PostAddResponse;
import com.example.mdmggreal.post.dto.response.PostGetListResponse;
import com.example.mdmggreal.post.dto.response.PostGetResponse;
import com.example.mdmggreal.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostAddResponse> postAdd(@RequestHeader(value = "Authorization") String token,
                                                   @RequestPart("uploadVideos") MultipartFile uploadVideos,
                                                   @RequestPart("content") MultipartFile contentFile,
                                                   @RequestPart("thumbnailImage") MultipartFile thumbnailImage,
                                                   @RequestPart("postAddRequest") PostAddRequest postAddRequest
    ) throws IOException {
        Long memberId = JwtUtil.getMemberId(token);
        if (uploadVideos == null || uploadVideos.isEmpty()) {
            throw new CustomException(ErrorCode.VIDEO_REQUIRED);
        }
        String content = new String(contentFile.getBytes(), StandardCharsets.UTF_8);

        postService.addPost(uploadVideos, thumbnailImage, postAddRequest, content, memberId);

        return ResponseEntity.ok(PostAddResponse.of(HttpStatus.CREATED));
    }

    @GetMapping("{postId}")
    public ResponseEntity<PostGetResponse> postGet(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable Long postId) {
        PostDTO post = postService.getPost(postId, token);
        return ResponseEntity.ok(PostGetResponse.from(OK, post));
    }

    @GetMapping
    public ResponseEntity<PostGetListResponse> postsGetOrderByCreatedDateTime(@RequestHeader(value = "Authorization", required = false) String token, @RequestParam("orderby") String orderBy, @RequestParam("keyword") String keyWord) {
        List<PostDTO> posts = postService.getPostsOrderByCreatedDateTime(token, orderBy, keyWord);
        return ResponseEntity.ok(PostGetListResponse.from(OK, posts));
    }

    @GetMapping("/users")
    public ResponseEntity<PostGetListResponse> postsGetByMember(@RequestHeader(value = "Authorization") String token) {
        Long memberId = JwtUtil.getMemberId(token);
        List<PostDTO> posts = postService.getPostsByMember(memberId);
        return ResponseEntity.ok(PostGetListResponse.from(OK, posts));
    }
}
