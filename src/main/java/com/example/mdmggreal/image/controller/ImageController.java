package com.example.mdmggreal.image.controller;

import com.example.mdmggreal.image.dto.request.ImageDeleteRequest;
import com.example.mdmggreal.image.service.ImageService;
import lombok.RequiredArgsConstructor;
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
    public List<String> imageUpload(@RequestPart(value = "file") List<MultipartFile> multipartFile) throws IOException {
        return imageService.uploadImage(multipartFile);
    }

    @DeleteMapping
    public ResponseEntity<?> imageDelete(@RequestBody ImageDeleteRequest request) {
        imageService.deleteImage(request);
        return ResponseEntity.ok().build();
    }
}
