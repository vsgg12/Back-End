package com.example.mdmggreal.test;

import com.example.mdmggreal.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final PostService postService;

    @PostMapping("/content")
    public String test(String content) {
        return "또다시 테스트~~!";
    }
    @GetMapping
    public ResponseEntity test() {
        postService.deletePost(63L, 13L);
        return ResponseEntity.ok().build();
    }
}
