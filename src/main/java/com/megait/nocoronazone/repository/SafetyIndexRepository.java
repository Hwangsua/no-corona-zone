package com.megait.nocoronazone.repository;

import com.megait.nocoronazone.domain.SafetyIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SafetyIndexRepository extends JpaRepository<SafetyIndex, String> {

    @Query(value = "SELECT MAX(index) FROM SafetyIndex")
    Double maxIndex();

    @Query(value = "SELECT MAX(confirmed) FROM SafetyIndex")
    int maxConfirmed();
}
