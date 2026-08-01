package com.fleetingtrails.fleetingjobsbackend.user.certification.repository;

import com.fleetingtrails.fleetingjobsbackend.user.certification.entity.CertificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CertificationRepository extends JpaRepository<CertificationEntity, Long> {
    List<CertificationEntity> findByUserId(Long userId);
}