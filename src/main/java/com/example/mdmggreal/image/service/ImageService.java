package com.example.mdmggreal.image.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.image.dto.request.ImageDeleteRequest;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    public List<String> uploadImage(List<MultipartFile> multipartFile, String mobile) throws IOException {
        Member member = getMember(mobile);
        return s3Service.uploadImages(multipartFile);
    }

    public void deleteImage(ImageDeleteRequest request, String mobile) {
        Member member = getMember(mobile);
        s3Service.delete(request);
    }
    private Member getMember(String mobile) {
        return memberRepository.findByMobile(mobile).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_USER_ID)
        );
    }
}
