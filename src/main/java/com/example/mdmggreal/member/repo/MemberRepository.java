package com.example.mdmggreal.member.repo;

import com.example.mdmggreal.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByNickname(String nickname);
    Member findByMemberId(String memberId);
    boolean existsByEmail(String email);
}
