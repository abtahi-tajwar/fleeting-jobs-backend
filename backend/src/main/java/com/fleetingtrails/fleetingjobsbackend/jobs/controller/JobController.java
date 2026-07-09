package com.fleetingtrails.fleetingjobsbackend.jobs.controller;

import com.fleetingtrails.fleetingjobsbackend.common.response.APIGetResponse;
import com.fleetingtrails.fleetingjobsbackend.jobs.service.JobService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
public class JobController {
    public final JobService jobService;
    public JobController(
            JobService jobService
    ) {
        this.jobService = jobService;
    }
    @GetMapping("search/test")
    public APIGetResponse<String> jobsSearchTest () {
        String data = jobService.testConnection();
        APIGetResponse<String> res = new APIGetResponse<String>();
        res.setSuccess(true);
        res.setData(data);
        return res;
    }
}
