package com.megait.nocoronazone.repository;

import com.megait.nocoronazone.domain.Mention;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MentionRepository extends JpaRepository<Mention, Long> {

        List<Mention> findByContentContaining(String keyword);

        List<Mention> findByMember_NicknameOrderByRegdateDesc(String nickname);

        List<Mention> findAll();

        List<Mention> deleteByNo(Long no);



}
