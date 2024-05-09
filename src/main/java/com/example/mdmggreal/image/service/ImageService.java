package com.example.mdmggreal.image.service;

import com.example.mdmggreal.amazon.service.S3Service;
import com.example.mdmggreal.image.dto.request.ImageDeleteRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ImageService {
    private final S3Service s3Service;

    public List<String> uploadImage(List<MultipartFile> multipartFile) throws IOException {

        return s3Service.uploadImages(multipartFile);
    }

    public void deleteImage(ImageDeleteRequest request) {

        s3Service.delete(request);
    }
}
