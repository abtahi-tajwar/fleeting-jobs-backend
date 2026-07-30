package com.fleetingtrails.fleetingjobsbackend.user.education.repository;

import com.fleetingtrails.fleetingjobsbackend.user.education.entity.EducationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<EducationEntity, Long> {
    List<EducationEntity> findByUserId(Long userId);
}