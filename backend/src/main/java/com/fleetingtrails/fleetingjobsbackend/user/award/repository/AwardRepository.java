package com.fleetingtrails.fleetingjobsbackend.user.award.repository;

import com.fleetingtrails.fleetingjobsbackend.user.award.entity.AwardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AwardRepository extends JpaRepository<AwardEntity, Long> {
    List<AwardEntity> findByUserId(Long userId);
}