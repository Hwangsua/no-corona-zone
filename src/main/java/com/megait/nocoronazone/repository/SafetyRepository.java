package com.megait.nocoronazone.repository;
import com.megait.nocoronazone.domain.SafetyIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SafetyRepository extends JpaRepository<SafetyIndex, String> {
}

