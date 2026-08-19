package com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.repository;

import com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.entity.AwardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AwardRepository extends JpaRepository<AwardEntity, Long> {
    List<AwardEntity> findByUserId(Long userId);
}