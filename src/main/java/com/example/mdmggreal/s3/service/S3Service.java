package com.example.mdmggreal.s3.service;

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

    public void deleteImages(ImageDeleteRequest request) {
        String splitStr = ".com/";
        request.getImageUrl().forEach(imageUrl -> {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf(splitStr) + splitStr.length());
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        });
    }

    /**
     * S3 이미지/동영상 삭제
     * - 삭제 후 복구 불가능!
     */
    public void deleteS3Objects(List<String> s3Urls) {
        s3Urls.forEach(s3Url -> {
            // S3 객체 경로 추출
            // ex) https://example.s3.example.amazonaws.com/example/cg123-772jsl-d33dg -> /example/cg123-772jsl-d33dg
            String[] splitArr = s3Url.split("com/");
            String objectUrl = splitArr[splitArr.length - 1];

            amazonS3.deleteObject(new DeleteObjectRequest(bucket, objectUrl));
        });
    }
}
