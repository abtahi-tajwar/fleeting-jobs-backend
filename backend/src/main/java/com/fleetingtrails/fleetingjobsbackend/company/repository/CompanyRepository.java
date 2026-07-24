package com.fleetingtrails.fleetingjobsbackend.company.repository;

import com.fleetingtrails.fleetingjobsbackend.company.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    List<CompanyEntity> findByLastScrapedAtIsNullOrLastScrapedAtBefore(
            LocalDateTime cutoff
    );
    Optional<CompanyEntity> findByListingUrl (String url);
}
