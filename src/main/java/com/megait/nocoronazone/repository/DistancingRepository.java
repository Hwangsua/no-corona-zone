package com.megait.nocoronazone.repository;

import com.megait.nocoronazone.domain.SocialDistancing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface DistancingRepository extends JpaRepository<SocialDistancing, String> {

    @Transactional
    @Modifying
    @Query("update SocialDistancing s set s.distancingNumber = ?1 where s.localName = ?2")
    void updateDistancing(String number, String name);

    @Query(value = "SELECT MAX(distancingNumber) FROM SocialDistancing ")
    String maxDistancing();

}
