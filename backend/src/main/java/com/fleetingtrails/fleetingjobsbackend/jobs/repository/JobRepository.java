package com.fleetingtrails.fleetingjobsbackend.jobs.repository;

import com.fleetingtrails.fleetingjobsbackend.jobs.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<JobEntity, Long> {
    List<JobEntity> findByDescriptionIsNull();
}
