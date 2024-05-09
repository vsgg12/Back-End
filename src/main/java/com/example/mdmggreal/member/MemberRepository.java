package com.example.mdmggreal.member;

import com.example.mdmggreal.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByNickname(String nickname);
    Member findByMemberId(String memberId);
    boolean existByMemberId(String memberId);
}
