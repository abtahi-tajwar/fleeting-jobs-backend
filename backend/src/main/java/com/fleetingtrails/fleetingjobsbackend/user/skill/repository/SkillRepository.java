package com.fleetingtrails.fleetingjobsbackend.user.skill.repository;

import com.fleetingtrails.fleetingjobsbackend.user.skill.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<SkillEntity, Long> {
    List<SkillEntity> findByUserId(Long userId);
}