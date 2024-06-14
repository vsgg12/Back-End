package com.example.mdmggreal.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.example.mdmggreal.image.dto.request.ImageDeleteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;
    private final TransferManager transferManager;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImages(MultipartFile multipartFile) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        String path = "image/" + UUID.randomUUID();

        Upload upload = transferManager.upload(bucket, path, multipartFile.getInputStream(), metadata);
        try {
            upload.waitForCompletion();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Upload interrupted", e);
        }

        return amazonS3.getUrl(bucket, path).toString();
    }

    public List<String> uploadImages(List<MultipartFile> multipartFileList) throws IOException {
        List<String> imageUrl = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFileList) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());
            String path = "image/" + UUID.randomUUID();

            Upload upload = transferManager.upload(bucket, path, multipartFile.getInputStream(), metadata);
            try {
                upload.waitForCompletion();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Upload interrupted", e);
            }

            imageUrl.add(amazonS3.getUrl(bucket, path).toString());
        }
        return imageUrl;
    }

    public String uploadVideo(MultipartFile multipartFile) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        String path = "video/" + UUID.randomUUID();

        Upload upload = transferManager.upload(bucket, path, multipartFile.getInputStream(), metadata);
        try {
            upload.waitForCompletion();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Upload interrupted", e);
        }

        return amazonS3.getUrl(bucket, path).toString();
    }

    public void delete(ImageDeleteRequest request) {
        String splitStr = ".com/";
        request.getImageUrl().forEach(imageUrl -> {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf(splitStr) + splitStr.length());
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        });
    }
}
