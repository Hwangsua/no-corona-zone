package com.megait.nocoronazone.repository;

import com.megait.nocoronazone.domain.Mention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentionRepository extends JpaRepository<Mention, Long> {
        List<Mention> findByContentContaining(String keyword);

        List<Mention> findAll();
}
