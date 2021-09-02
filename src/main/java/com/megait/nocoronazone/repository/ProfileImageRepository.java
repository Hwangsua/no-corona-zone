package com.megait.nocoronazone.repository;

import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.domain.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    Optional<ProfileImage> findByMember(Member member);
}
