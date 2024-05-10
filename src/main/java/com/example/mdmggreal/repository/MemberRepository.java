package com.example.mdmggreal.repository;

import com.example.mdmggreal.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existByNickname(String nickname);
}
