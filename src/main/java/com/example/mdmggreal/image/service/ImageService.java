package com.example.mdmggreal.image.service;

import com.example.mdmggreal.image.dto.request.ImageDeleteRequest;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.service.MemberGetService;
import com.example.mdmggreal.s3.service.S3Service;
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
    private final MemberGetService memberGetService;

    public List<String> uploadImage(List<MultipartFile> multipartFile, Long memberId) throws IOException {
        Member member = memberGetService.getMemberByIdOrThrow(memberId);
        return s3Service.uploadImages(multipartFile);
    }

    public void deleteImage(ImageDeleteRequest request, Long memberId) {
        Member member = memberGetService.getMemberByIdOrThrow(memberId);
        s3Service.deleteImages(request);
    }
}
