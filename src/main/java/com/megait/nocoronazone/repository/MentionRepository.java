package com.megait.nocoronazone.repository;

import com.megait.nocoronazone.domain.Mention;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MentionRepository extends JpaRepository<Mention, Long> {
        List<Mention> findByContentContaining(String keyword);
        List<Mention> findAll();
}
