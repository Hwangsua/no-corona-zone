package com.megait.nocoronazone.repository;
import com.megait.nocoronazone.domain.SafetyIndex;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SafetyRepository extends JpaRepository<SafetyIndex, Long> {
}

