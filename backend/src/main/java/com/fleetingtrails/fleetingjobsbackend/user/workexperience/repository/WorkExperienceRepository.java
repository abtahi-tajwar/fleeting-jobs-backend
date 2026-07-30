package com.fleetingtrails.fleetingjobsbackend.user.workexperience.repository;

import com.fleetingtrails.fleetingjobsbackend.user.workexperience.entity.WorkExperienceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperienceEntity, Long> {
    List<WorkExperienceEntity> findByUserId(Long userId);
}