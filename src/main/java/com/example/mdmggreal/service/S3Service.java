package com.example.mdmggreal.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
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

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImages(MultipartFile multipartFile) throws IOException {

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        String path = "image/" + UUID.randomUUID();

        amazonS3.putObject(bucket, path, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, path).toString();
    }

    public List<String> uploadImages(List<MultipartFile> multipartFileList) throws IOException {
        List<String> imageUrl = new ArrayList<>();
        multipartFileList.forEach(multipartFile -> {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());
            String path = "image/" + UUID.randomUUID();
            try {
                amazonS3.putObject(bucket, path, multipartFile.getInputStream(), metadata);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imageUrl.add(amazonS3.getUrl(bucket, path).toString());
        });

        return imageUrl;


    }

    public String uploadVideo(MultipartFile multipartFile) throws IOException {

        TransferManager transferManager = TransferManagerBuilder.standard()
                .withS3Client(amazonS3)
                .build();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        String path = "video/" + UUID.randomUUID();

        Upload upload = transferManager.upload(bucket, path, multipartFile.getInputStream(), metadata);

        try {
            // Wait for the upload to complete
            upload.waitForCompletion();
        } catch (InterruptedException e) {
            // Handle interruption
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
