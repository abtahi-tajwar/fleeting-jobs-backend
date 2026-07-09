package com.fleetingtrails.fleetingjobsbackend.jobs.service;

import com.fleetingtrails.fleetingjobsbackend.common.services.WorkerService;
import org.springframework.stereotype.Service;

@Service
public class JobService {
    public final WorkerService workerService;

    public JobService (
            WorkerService workerService
    ) {
        this.workerService = workerService;
    }

    public String testConnection () {
        return this.workerService.webClient.get().uri("/jobs/search/5").retrieve()
                .bodyToMono(String.class)
                .block();

    }
}
