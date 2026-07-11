package com.fleetingtrails.fleetingjobsbackend.jobs.service;

import com.fleetingtrails.fleetingjobsbackend.common.services.WorkerService;
import com.fleetingtrails.fleetingjobsbackend.jobs.dto.JobListItemDto;
import com.fleetingtrails.fleetingjobsbackend.jobs.entity.JobEntity;
import com.fleetingtrails.fleetingjobsbackend.jobs.mapper.JobMapper;
import com.fleetingtrails.fleetingjobsbackend.jobs.repository.JobRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {
    public final WorkerService workerService;
    public final JobRepository jobRepository;
    public final JobMapper jobMapper;

    public JobService (
            WorkerService workerService,
            JobRepository jobRepository,
            JobMapper jobMapper
    ) {
        this.workerService = workerService;
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
    }

    public List<JobListItemDto> testConnection () {
        List<JobListItemDto> res = this.workerService.webClient.get().uri("/jobs/search/5").retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<JobListItemDto>>() {})
                .block();

        List<JobEntity> jobs = new ArrayList<>();
        for (JobListItemDto item : res) {
            jobs.add(jobMapper.toJobEntity(item));
        }
        jobRepository.saveAll(jobs);

        return res;
    }



    public List<JobListItemDto> getJobs () {
        List<JobEntity> jobsResponse = jobRepository.findAll();
        List<JobListItemDto> jobs = new ArrayList<>();

        for (JobEntity job : jobsResponse) {
            JobListItemDto item = jobMapper.toJobListItemDto(job);
            jobs.add(item);
        }

        return jobs;

    }
}
