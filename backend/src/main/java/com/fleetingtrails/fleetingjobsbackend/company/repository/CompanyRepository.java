package com.fleetingtrails.fleetingjobsbackend.company.repository;

import com.fleetingtrails.fleetingjobsbackend.company.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    
}
