package com.fleetingtrails.fleetingjobsbackend.common.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WorkerService {
    public final WebClient webClient;
    public WorkerService(
            @Qualifier("workerWebClient")
            WebClient workerWebClient) {
        this.webClient = workerWebClient;

    }
}
