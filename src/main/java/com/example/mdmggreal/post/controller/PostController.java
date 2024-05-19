package com.example.mdmggreal.post.controller;

import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.ingameinfo.dto.response.InGameInfoResponse;
import com.example.mdmggreal.ingameinfo.service.InGameInfoService;
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
    private final InGameInfoService inGameInfoService;

    @PostMapping
    public ResponseEntity<PostAddResponse> postAdd(@RequestHeader(value = "Authorization") String token,
                                                   @RequestPart("uploadVideos") MultipartFile uploadVideos,
                                                   @RequestPart("content") MultipartFile contentFile,
                                                   @RequestPart("thumbnailImage") MultipartFile thumbnailImage,
                                                   @RequestPart("postAddRequest") PostAddRequest postAddRequest
    ) throws IOException {
        JwtUtil.validateToken(token);
        String mobile = JwtUtil.getMobile(token);

        try {
            String content = new String(contentFile.getBytes(), StandardCharsets.UTF_8);
            postService.addPost(uploadVideos, thumbnailImage, postAddRequest, content, mobile);
            // content 처리 로직
        } catch (IOException e) {
            // 처리 중 에러 발생 시 처리
            e.printStackTrace();
        }

        return ResponseEntity.ok(PostAddResponse.of(HttpStatus.CREATED));
    }

    @GetMapping("{postId}")
    public ResponseEntity<PostGetResponse> postGet(@RequestHeader(value = "Authorization") String token, @PathVariable Long postId) {
        JwtUtil.validateToken(token);
        String mobile = JwtUtil.getMobile(token);
        PostDTO post = postService.getPost(postId, mobile);
        List<InGameInfoResponse> inGameInfo = inGameInfoService.getInGameInfo(postId);

        return ResponseEntity.ok(PostGetResponse.from(OK, post, inGameInfo));
    }

    @GetMapping
    public ResponseEntity<PostGetListResponse> postsGetOrderByCreatedDateTime(@RequestHeader(value = "Authorization") String token, @RequestParam("orderby") String orderBy) {
        JwtUtil.validateToken(token);
        String mobile = JwtUtil.getMobile(token);
        List<PostDTO> posts = postService.getPostsOrderByCreatedDateTime(mobile, orderBy);
        return ResponseEntity.ok(PostGetListResponse.from(OK, posts));
    }

    @GetMapping("/search")
    public ResponseEntity<PostGetListResponse> postsGetKeyword(@RequestHeader(value = "Authorization") String token, @RequestParam("keyword") String keyWord) {
        JwtUtil.validateToken(token);
        String mobile = JwtUtil.getMobile(token);
        List<PostDTO> posts = postService.getPostsKeyword(mobile, keyWord);
        return ResponseEntity.ok(PostGetListResponse.from(OK, posts));
    }


    @GetMapping("/users")
    public ResponseEntity<PostGetListResponse> postsGetByMember(@RequestHeader(value = "Authorization") String token) {

        JwtUtil.validateToken(token);
        String mobile = JwtUtil.getMobile(token);
        List<PostDTO> posts = postService.getPostsByMember(mobile);
        return ResponseEntity.ok(PostGetListResponse.from(OK, posts));
    }
}
