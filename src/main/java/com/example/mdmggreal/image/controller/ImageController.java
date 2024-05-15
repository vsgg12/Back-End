package com.example.mdmggreal.image.controller;

import com.example.mdmggreal.global.response.BaseResponse;
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

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ImageUploadResponse> imageUpload(@RequestPart(value = "file") List<MultipartFile> multipartFile) throws IOException {
        List<String> ImageUrlList = imageService.uploadImage(multipartFile);
        return ResponseEntity.ok(ImageUploadResponse.from(ImageUrlList, HttpStatus.OK));
    }

    @DeleteMapping
    public ResponseEntity<BaseResponse> imageDelete(@RequestBody ImageDeleteRequest request) {
        imageService.deleteImage(request);
        return ResponseEntity.ok(BaseResponse.from(HttpStatus.OK));
    }
}
