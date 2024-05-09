package com.example.mdmggreal.post.controller;

import com.example.mdmggreal.post.dto.PostAddRequest;
import com.example.mdmggreal.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

//    @PostMapping
//    public ResponseEntity<?> postAdd(@RequestHeader(value = "Authorization") String token,
//                                     @RequestPart MultipartFile uploadVideos,
//                                     @RequestPart MultipartFile thumbnailImage,
//                                     @RequestPart PostAddRequest postAddRequest
//    ) throws IOException {
//        postService.addPost(token, uploadVideos, thumbnailImage, postAddRequest);
//        // 멤버 아이디 가져오는거
//        String email = "jawoo1003@gmail.com";
//
//        return ResponseEntity.ok().build();
//    }

    @PostMapping
    public ResponseEntity<?> postAdd(@RequestPart MultipartFile uploadVideos,
                                     @RequestPart MultipartFile thumbnailImage,
                                     @RequestPart PostAddRequest postAddRequest
    ) throws IOException {
        postService.addPost(uploadVideos, thumbnailImage, postAddRequest);
        // 멤버 아이디 가져오는거
        String email = "jawoo1003@gmail.com";

        return ResponseEntity.ok().build();
    }
}
