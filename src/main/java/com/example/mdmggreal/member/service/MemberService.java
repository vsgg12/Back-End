package com.example.mdmggreal.member.service;

import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.global.exception.ErrorCode;
import com.example.mdmggreal.global.security.CustomUserInfoDto;
import com.example.mdmggreal.global.security.JwtUtil;
import com.example.mdmggreal.member.dto.MemberDTO;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;


    public String checkMobile(String mobile) {
        if (memberRepository.existsByMobile(mobile)) {
            Member member = memberRepository.findByMobile(mobile).get();

            return jwtUtil.createAccessToken(CustomUserInfoDto.of(member));
        } else {
            return null;
        }
    }

    public String checkEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            Member member = memberRepository.findByEmail(email).get();

            return jwtUtil.createAccessToken(CustomUserInfoDto.of(member));
        } else {
            return null;
        }
    }

    public void signup(MemberDTO memberDTO) {
        if (memberRepository.existsByMobile(memberDTO.getMobile())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }
        memberRepository.save(Member.from(memberDTO));
    }

    public Boolean checkNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            return true;
        } else {
            return false;
        }
    }

}
