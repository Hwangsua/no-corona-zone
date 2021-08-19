package com.megait.nocoronazone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.megait.nocoronazone.domain.SafetyIndexInSeoul;

public interface Seoul_SafetyRepository extends JpaRepository<SafetyIndexInSeoul, Long> {
}
