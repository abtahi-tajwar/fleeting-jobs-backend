package com.fleetingtrails.fleetingjobsbackend.jobs.controller;

import com.fleetingtrails.fleetingjobsbackend.common.response.APIGetResponse;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIPostResponse;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.dto.RequestJobDetailsMessageDto;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.producer.RabbitProducerService;
import com.fleetingtrails.fleetingjobsbackend.jobs.dto.JobListItemDto;
import com.fleetingtrails.fleetingjobsbackend.jobs.service.JobService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {
    private final JobService jobService;
    public JobController(
            JobService jobService
    ) {
        this.jobService = jobService;
    }

    @GetMapping("/list")
    public APIGetResponse<List<JobListItemDto>> getAllJobs () {
        List<JobListItemDto> data = jobService.getJobs();
        APIGetResponse<List<JobListItemDto>> res = new APIGetResponse<>();

        return APIGetResponse.success(data);
    }

    @PostMapping("process/fetch/description")
    public List<JobListItemDto> processJobDescriptionFetch () {
        return jobService.processJobDescriptionFetch();
    }
    @PostMapping("process/fetch/jobs")
    public APIPostResponse<String> processFetchJobs () {
        try {
            jobService.processFetchJobs();
            return APIPostResponse.success("Job Fetch queried successfully");
        } catch (Exception e) {
            return APIPostResponse.failed(e.getMessage());
        }
    }
}
