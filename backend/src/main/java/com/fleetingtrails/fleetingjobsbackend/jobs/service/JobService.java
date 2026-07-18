package com.fleetingtrails.fleetingjobsbackend.jobs.service;

import com.fleetingtrails.fleetingjobsbackend.common.services.WorkerService;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.dto.ReceiveJobDetailsMessageDto;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.dto.RequestJobDetailsMessageDto;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.producer.RabbitProducerService;
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
    private final WorkerService workerService;
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final RabbitProducerService rabbitProducerService;

    public JobService (
            WorkerService workerService,
            JobRepository jobRepository,
            JobMapper jobMapper,
            RabbitProducerService rabbitProducerService
    ) {
        this.workerService = workerService;
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
        this.rabbitProducerService = rabbitProducerService;
    }

    // Needs update
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

    public void receiveJobDetails (ReceiveJobDetailsMessageDto message) {
        String description = message.getDescription();
        Long id = message.getId();
        jobRepository.findById(id).ifPresent(job -> {
            job.setDescription(description);
            jobRepository.save(job);
            System.out.printf("Saved description for job %s with id %d", job.getTitle(), job.getId());
        });
    }

    public List<JobListItemDto> processJobDescriptionFetch () {
        List<JobEntity> res = jobRepository.findByDescriptionIsNull();
        List<JobListItemDto> jobs = new ArrayList<>();
        RequestJobDetailsMessageDto message = new RequestJobDetailsMessageDto();
        for (JobEntity job : res) {
            jobs.add(jobMapper.toJobListItemDto(job));
            message.setUrl(job.getUrl());
            message.setId(job.getId());
            rabbitProducerService.requestJobDetails(message);
        }
        return jobs;
    }
}
