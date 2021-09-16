package com.megait.nocoronazone.repository;

import com.megait.nocoronazone.domain.Mention;
import com.megait.nocoronazone.domain.ReMention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReMentionRepository extends JpaRepository<ReMention, Long> {
    List<ReMention> findByMentionOrderByRegdateDesc(Mention mention);
    List<ReMention> deleteByNo(Long no);
}
