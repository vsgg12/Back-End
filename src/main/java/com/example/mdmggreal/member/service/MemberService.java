package com.example.mdmggreal.member.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.global.security.CustomUserInfoDto;
import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.member.dto.MemberDTO;
import com.example.mdmggreal.member.dto.request.SignUpRequest;
import com.example.mdmggreal.member.dto.response.SignUpResponse;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import com.example.mdmggreal.oauth.dto.AuthTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public SignUpResponse signUp(SignUpRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }
        Member savedMember = memberRepository.save(Member.from(request));

        AuthTokens createdTokens = jwtUtil.createTokens(CustomUserInfoDto.of(savedMember));

        return SignUpResponse.from(savedMember, createdTokens);
    }

    public Boolean checkNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            return true;
        } else {
            return false;
        }
    }

    public MemberDTO memberGet(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.INVALID_USER_ID)
        );
        return MemberDTO.from(member);
    }
}
