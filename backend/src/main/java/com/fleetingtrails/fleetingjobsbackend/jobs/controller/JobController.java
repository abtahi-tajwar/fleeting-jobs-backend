package com.fleetingtrails.fleetingjobsbackend.jobs.controller;

import com.fleetingtrails.fleetingjobsbackend.auth.annotation.Authorize;
import com.fleetingtrails.fleetingjobsbackend.common.AppModule;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIGetResponse;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIPostResponse;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.dto.RequestJobDetailsMessageDto;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.producer.RabbitProducerService;
import com.fleetingtrails.fleetingjobsbackend.jobs.dto.JobListItemDto;
import com.fleetingtrails.fleetingjobsbackend.jobs.service.JobService;
import org.springframework.web.bind.annotation.*;

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

    @Authorize(
            module = AppModule.JOBS,
            action = "LIST"
    )
    @GetMapping("/list")
    public APIGetResponse<List<JobListItemDto>> getAllJobs () {
        List<JobListItemDto> data = jobService.getJobs();
        APIGetResponse<List<JobListItemDto>> res = new APIGetResponse<>();

        return APIGetResponse.success(data);
    }

    @Authorize(
            module = AppModule.JOBS,
            action = "PROCESS"
    )
    @PostMapping("process/fetch/description")
    public List<JobListItemDto> processJobDescriptionFetch () {
        return jobService.processJobDescriptionFetch();
    }

    @Authorize(
            module = AppModule.JOBS,
            action = "PROCESS"
    )
    @PostMapping("process/fetch/jobs")
    public APIPostResponse<String> processFetchJobs () {
        try {
            jobService.processFetchJobs();
            return APIPostResponse.success("Job Fetch queried successfully");
        } catch (Exception e) {
            return APIPostResponse.failed(e.getMessage());
        }
    }

    @Authorize(
            module = AppModule.JOBS,
            action = "PROCESS"
    )
    @PostMapping("process/fetch/jobs_by_company/{companyId}")
    public APIPostResponse<String> processFetchJobsByCompany (
            @RequestBody long companyId
    ) {
        try {
            jobService.processFetchJobsByCompany(companyId);
            return APIPostResponse.success("Job Fetch queried successfully");
        } catch (Exception e) {
            return APIPostResponse.failed(e.getMessage());
        }
    }
}
