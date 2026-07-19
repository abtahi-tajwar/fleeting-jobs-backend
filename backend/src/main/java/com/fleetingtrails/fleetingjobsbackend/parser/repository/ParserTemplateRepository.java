package com.fleetingtrails.fleetingjobsbackend.parser.repository;

import com.fleetingtrails.fleetingjobsbackend.parser.entity.ParserTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ParserTemplateRepository
        extends JpaRepository<ParserTemplateEntity, Long>, JpaSpecificationExecutor<ParserTemplateEntity> {
    boolean existsByCompanyId(Long companyId);
}
