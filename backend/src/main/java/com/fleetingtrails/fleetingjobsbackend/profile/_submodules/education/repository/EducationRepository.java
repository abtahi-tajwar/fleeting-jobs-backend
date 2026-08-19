package com.fleetingtrails.fleetingjobsbackend.profile._submodules.education.repository;

import com.fleetingtrails.fleetingjobsbackend.profile._submodules.education.entity.EducationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<EducationEntity, Long> {
    List<EducationEntity> findByUserId(Long userId);
}