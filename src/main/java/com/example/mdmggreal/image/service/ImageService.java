package com.example.mdmggreal.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.mdmggreal.image.dto.request.ImageDeleteRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ImageService {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile multipartFile) throws IOException {

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        String path = "tmp/" + UUID.randomUUID().toString();

        amazonS3.putObject(bucket, path, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, path).toString();
    }

    public void deleteImage(ImageDeleteRequest request) {

        String splitStr = ".com/";
        request.getImageUrl().forEach(imageUrl -> {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf(splitStr) + splitStr.length());
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        });
    }
}
