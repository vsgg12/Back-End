package com.example.mdmggreal.service;
import com.example.mdmggreal.member.entity.Member;
import com.example.mdmggreal.member.repo.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {

    }

    @Test
    void signup() {
        Member member = new Member();
        member.setMemberId("TEST");
        member.setEmail("aa");
        member.setNickname("ABC");

        memberRepository.save(member);
    }

    @Test
    void findNickName() {
        Member member = new Member();
        member.setNickname("ABC");
        boolean existNickName = memberRepository.existsByNickname(member.getNickname());

        System.out.println(existNickName);
    }
}
