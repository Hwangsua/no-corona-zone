package com.megait.nocoronazone.repository;

import com.megait.nocoronazone.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{

    Optional<Member> findByNo(Long no);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);

}
