package com.megait.nocoronazone.repository;

import com.megait.nocoronazone.domain.DetailSafetyIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface DetailSafetyRepository extends JpaRepository<DetailSafetyIndex, String>{

    @Query(value = "SELECT MAX(index) FROM DetailSafetyIndex")
    Double maxIndex();
}
