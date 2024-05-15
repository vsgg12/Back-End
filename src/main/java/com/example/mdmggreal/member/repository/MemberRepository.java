package com.example.mdmggreal.member.repository;

import com.example.mdmggreal.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByNickname(String nickname);
    boolean existsByMobile(String mobile);
    boolean existsByToken(String token);
}
