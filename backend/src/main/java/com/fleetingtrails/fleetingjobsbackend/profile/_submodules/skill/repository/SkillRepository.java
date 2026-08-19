package com.fleetingtrails.fleetingjobsbackend.profile._submodules.skill.repository;

import com.fleetingtrails.fleetingjobsbackend.profile._submodules.skill.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<SkillEntity, Long> {
    List<SkillEntity> findByUserId(Long userId);
}