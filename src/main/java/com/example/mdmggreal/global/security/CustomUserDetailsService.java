package com.example.mdmggreal.global.security;

import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        Member member = memberRepository.findByMobile(mobile).orElseThrow(
                () -> new UsernameNotFoundException(mobile)
        );
        CustomUserInfoDto customUserInfoDto = CustomUserInfoDto.of(member);
        return new CustomUserDetails(customUserInfoDto);

    }

}
