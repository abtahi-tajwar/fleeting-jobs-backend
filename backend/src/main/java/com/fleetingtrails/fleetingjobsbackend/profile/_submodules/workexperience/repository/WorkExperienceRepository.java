package com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.repository;

import com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.entity.WorkExperienceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperienceEntity, Long> {
    List<WorkExperienceEntity> findByUserId(Long userId);
}