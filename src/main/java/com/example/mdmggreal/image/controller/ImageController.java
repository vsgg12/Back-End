package com.example.mdmggreal.image.controller;

import com.example.mdmggreal.global.response.BaseResponse;
import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.image.dto.request.ImageDeleteRequest;
import com.example.mdmggreal.image.dto.response.ImageUploadResponse;
import com.example.mdmggreal.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ImageUploadResponse> imageUpload(
            @RequestHeader(value = "Authorization") String token,
            @RequestPart(value = "file") List<MultipartFile> multipartFile) throws IOException {
        Long memberId = JwtUtil.getMemberId(token);
        List<String> ImageUrlList = imageService.uploadImage(multipartFile, memberId);
        return ResponseEntity.ok(ImageUploadResponse.from(ImageUrlList, HttpStatus.OK));
    }

    @DeleteMapping
    public ResponseEntity<BaseResponse> imageDelete(@RequestHeader(value = "Authorization") String token, @RequestBody ImageDeleteRequest request) {
        Long memberId = JwtUtil.getMemberId(token);
        imageService.deleteImage(request, memberId);
        return BaseResponse.toResponseEntity(OK);
    }
}
