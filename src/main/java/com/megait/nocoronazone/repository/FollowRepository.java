package com.megait.nocoronazone.repository;

import com.megait.nocoronazone.domain.Follow;
import com.megait.nocoronazone.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByWho(Member member);

    Integer countByWhom(Member member);

    List<Follow> findByWhom(Member member);

}
