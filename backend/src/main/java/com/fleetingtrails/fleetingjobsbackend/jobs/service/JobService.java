package com.fleetingtrails.fleetingjobsbackend.jobs.service;

import com.fleetingtrails.fleetingjobsbackend.common.services.WorkerService;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.dto.ReceiveJobDetailsMessageDto;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.dto.ReceiveNewJobListingMessageDto;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.dto.RequestJobDetailsMessageDto;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.producer.RabbitProducerService;
import com.fleetingtrails.fleetingjobsbackend.jobs.constants.JobConstants;
import com.fleetingtrails.fleetingjobsbackend.jobs.dto.JobListItemDto;
import com.fleetingtrails.fleetingjobsbackend.jobs.entity.JobEntity;
import com.fleetingtrails.fleetingjobsbackend.jobs.mapper.JobMapper;
import com.fleetingtrails.fleetingjobsbackend.jobs.repository.JobRepository;
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

    public void processFetchJobs () {
        this.workerService.webClient.get()
                .uri("/jobs/search/5")
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void handleRecieveNewJobListing (ReceiveNewJobListingMessageDto message) {
        JobEntity entity = new JobEntity();
        entity.setTitle(message.getTitle());
        entity.setUrl(message.getUrl());
        jobRepository.save(entity);
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
        // for test only
        int count = 0;
        for (JobEntity job : res) {
            if (count == JobConstants.JOB_DESCRIPTION_FETCH_LIMIT) break;
            jobs.add(jobMapper.toJobListItemDto(job));
            message.setUrl(job.getUrl());
            message.setId(job.getId());
            rabbitProducerService.requestJobDetails(message);
            count += 1;
        }
        return jobs;
    }
}
