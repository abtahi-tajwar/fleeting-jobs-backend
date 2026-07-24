package com.fleetingtrails.fleetingjobsbackend.jobs.service;

import com.fleetingtrails.fleetingjobsbackend.common.services.WorkerService;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.dto.ReceiveJobDetailsMessageDto;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.dto.ReceiveNewJobListingMessageDto;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.dto.RequestJobDetailsMessageDto;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.producer.RabbitProducerService;
import com.fleetingtrails.fleetingjobsbackend.company.entity.CompanyEntity;
import com.fleetingtrails.fleetingjobsbackend.company.repository.CompanyRepository;
import com.fleetingtrails.fleetingjobsbackend.jobs.constants.JobConstants;
import com.fleetingtrails.fleetingjobsbackend.jobs.dto.JobListItemDto;
import com.fleetingtrails.fleetingjobsbackend.jobs.entity.JobEntity;
import com.fleetingtrails.fleetingjobsbackend.jobs.mapper.JobMapper;
import com.fleetingtrails.fleetingjobsbackend.jobs.repository.JobRepository;
import com.fleetingtrails.fleetingjobsbackend.parser.dto.ScrapeJobListBodyDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {
    private final WorkerService workerService;
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final RabbitProducerService rabbitProducerService;
    private final CompanyRepository companyRepository;

    public JobService(
            WorkerService workerService,
            JobRepository jobRepository,
            JobMapper jobMapper,
            RabbitProducerService rabbitProducerService,
            CompanyRepository companyRepository
    ) {
        this.workerService = workerService;
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
        this.rabbitProducerService = rabbitProducerService;
        this.companyRepository = companyRepository;
    }

    public void processFetchJobs() {
        LocalDateTime cutoff = LocalDateTime.now().minus(Duration.ofMillis(JobConstants.SCRAPE_INTERVAL_MS));
        List<CompanyEntity> companiesToScrape = companyRepository.findByLastScrapedAtIsNullOrLastScrapedAtBefore(cutoff);
        for (CompanyEntity company : companiesToScrape) {
            ScrapeJobListBodyDto body = new ScrapeJobListBodyDto();
            body.setCompany_id(company.getId());
            body.setListing_url(company.getListingUrl());
            body.setParser_template(company.getParserTemplate().getConfig());

            this.workerService.webClient.post()
                    .uri("/jobs/scrape-list/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            company.setLastScrapedAt(LocalDateTime.now());
            companyRepository.save(company);
        }
    }


    public void handleRecieveNewJobListing(ReceiveNewJobListingMessageDto message) {
        JobEntity entity = new JobEntity();
        try {
            CompanyEntity companyEntity = companyRepository
                    .findById(message.getCompany_id())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Company does not exist: " + message.getCompany_id()
                    ));

            entity.setTitle(message.getTitle());
            entity.setUrl(message.getUrl());
            entity.setCompany(companyEntity);
            jobRepository.save(entity);
        } catch (Exception e) {
            System.out.printf("Company %s doesn't exist", message.getCompany_id());
        }
    }


    public List<JobListItemDto> getJobs() {
        List<JobEntity> jobsResponse = jobRepository.findAll();
        List<JobListItemDto> jobs = new ArrayList<>();

        for (JobEntity job : jobsResponse) {
            JobListItemDto item = jobMapper.toJobListItemDto(job);
            jobs.add(item);
        }

        return jobs;
    }

    public void receiveJobDetails(ReceiveJobDetailsMessageDto message) {
        String description = message.getDescription();
        Long id = message.getId();
        jobRepository.findById(id).ifPresent(job -> {
            job.setDescription(description);
            jobRepository.save(job);
            System.out.printf("Saved description for job %s with id %d", job.getTitle(), job.getId());
        });
    }

    public List<JobListItemDto> processJobDescriptionFetch() {
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
