package com.fleetingtrails.fleetingjobsbackend.jobs.service;

import com.fleetingtrails.fleetingjobsbackend.common.services.WorkerService;
import com.fleetingtrails.fleetingjobsbackend.jobs.entity.JobEntity;
import com.fleetingtrails.fleetingjobsbackend.jobs.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {
    public final WorkerService workerService;
    public final JobRepository jobRepository;

    public JobService (
            WorkerService workerService,
            JobRepository jobRepository
    ) {
        this.workerService = workerService;
        this.jobRepository = jobRepository;
    }

    public String testConnection () {
        return this.workerService.webClient.get().uri("/jobs/search/5").retrieve()
                .bodyToMono(String.class)
                .block();

    }

    public List<JobEntity> getJobs () {
        return jobRepository.findAll();
    }
}
