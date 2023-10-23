package com.space000.repository;

import com.space000.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);


    Member findByNickname(String nickname);
}
