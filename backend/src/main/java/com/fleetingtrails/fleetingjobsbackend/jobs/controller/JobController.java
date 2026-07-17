package com.fleetingtrails.fleetingjobsbackend.jobs.controller;

import com.fleetingtrails.fleetingjobsbackend.common.response.APIGetResponse;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.dto.RequestJobDetailsMessageDto;
import com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.producer.RabbitProducerService;
import com.fleetingtrails.fleetingjobsbackend.jobs.dto.JobListItemDto;
import com.fleetingtrails.fleetingjobsbackend.jobs.service.JobService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {
    private final JobService jobService;
    private final RabbitProducerService rabbitProducerService;
    public JobController(
            JobService jobService,
            RabbitProducerService rabbitProducerService
    ) {
        this.jobService = jobService;
        this.rabbitProducerService = rabbitProducerService;
    }
    @GetMapping("search/test")
    public APIGetResponse<List<JobListItemDto>> jobsSearchTest () {
        List<JobListItemDto> data = jobService.testConnection();
        APIGetResponse<List<JobListItemDto>> res = new APIGetResponse<>();
        APIGetResponse.success(res, "Job Scraped Successfully");
        return res;
    }

    @GetMapping("/list")
    public APIGetResponse<List<JobListItemDto>> getAllJobs () {
        List<JobListItemDto> data = jobService.getJobs();
        APIGetResponse<List<JobListItemDto>> res = new APIGetResponse<>();

//        APIGetResponse.success(jobService.getJobs());
        res.setData(data);
        res.setSuccess(true);
        return res;
    }

    @GetMapping("testrabit")
    public void testRabbit() {
        RequestJobDetailsMessageDto message = new RequestJobDetailsMessageDto();
        message.setId(3L);
        message.setUrl("https://google.com/");

        rabbitProducerService.requestJobDetails(message);
    }
}
