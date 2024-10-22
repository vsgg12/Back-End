package com.example.mdmggreal.image.service;

import com.example.mdmggreal.global.exception.CustomException;
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

import static com.example.mdmggreal.global.exception.ErrorCode.INVALID_USER_ID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ImageService {
    private final S3Service s3Service;
    private final MemberRepository memberRepository;

    public List<String> uploadImage(List<MultipartFile> multipartFile, Long memberId) throws IOException {
        Member member = getMemberByMemberId(memberId);
        return s3Service.uploadImages(multipartFile);
    }

    public void deleteImage(ImageDeleteRequest request, Long memberId) {
        Member member = getMemberByMemberId(memberId);
        s3Service.deleteImages(request);
    }

    private Member getMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(INVALID_USER_ID)
        );
    }
}
