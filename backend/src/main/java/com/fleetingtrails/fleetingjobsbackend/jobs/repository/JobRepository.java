package com.fleetingtrails.fleetingjobsbackend.jobs.repository;

import com.fleetingtrails.fleetingjobsbackend.jobs.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<JobEntity, Long> {

}
