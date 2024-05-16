package com.example.mdmggreal.post.controller;

import com.example.mdmggreal.post.dto.PostDTO;
import com.example.mdmggreal.post.dto.request.PostAddRequest;
import com.example.mdmggreal.post.dto.response.PostAddResponse;
import com.example.mdmggreal.post.dto.response.PostGetResponse;
import com.example.mdmggreal.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.mdmggreal.member.service.MemberService.getToken;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostAddResponse> postAdd(HttpServletRequest request,
                                                   @RequestPart MultipartFile uploadVideos,
                                                   @RequestPart MultipartFile thumbnailImage,
                                                   @RequestPart PostAddRequest postAddRequest
    ) throws IOException {
        String token = getToken(request);

        postService.addPost(uploadVideos, thumbnailImage, postAddRequest, token);
        return ResponseEntity.ok(PostAddResponse.of(HttpStatus.CREATED));
    }

    @GetMapping("{postId}")
    public ResponseEntity<PostGetResponse> postGet(HttpServletRequest request, @PathVariable Long postId) {
        String token = getToken(request);
        PostDTO post = postService.getPost(postId, token);

        return ResponseEntity.ok(PostGetResponse.from(OK, post));
    }

    @GetMapping
    public ResponseEntity<List<PostGetResponse>> postsGetOrderByCreatedDateTime(HttpServletRequest request, @RequestParam("orderby") String orderBy) {
        String token = getToken(request);
        List<PostDTO> posts = postService.getPostsOrderByCreatedDateTime(token, orderBy);
        List<PostGetResponse> postGetResponses = new ArrayList<>();
        posts.forEach(post -> {
            postGetResponses.add(PostGetResponse.from(OK, post));
        });
        return ResponseEntity.ok(postGetResponses);
    }
    @GetMapping("/search")
    public ResponseEntity<List<PostGetResponse>> postsGetKeyword(HttpServletRequest request, @RequestParam("keyword") String keyWord) {
        String token = getToken(request);
        List<PostDTO> posts = postService.getPostsKeyword(token, keyWord);
        List<PostGetResponse> postGetResponses = new ArrayList<>();
        posts.forEach(post -> {
            postGetResponses.add(PostGetResponse.from(OK, post));
        });
        return ResponseEntity.ok(postGetResponses);
    }




    @GetMapping("/users")
    public ResponseEntity<List<PostGetResponse>> postsGetByMember(HttpServletRequest request) {

        String token = getToken(request);
        List<PostDTO> posts = postService.getPostsByMember(token);
        List<PostGetResponse> postGetResponses = new ArrayList<>();
        posts.forEach(post -> {
            postGetResponses.add(PostGetResponse.from(OK, post));
        });
        return ResponseEntity.ok(postGetResponses);
    }
}
