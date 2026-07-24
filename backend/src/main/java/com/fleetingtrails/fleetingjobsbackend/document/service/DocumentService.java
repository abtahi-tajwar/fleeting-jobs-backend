package com.fleetingtrails.fleetingjobsbackend.document.service;

import com.fleetingtrails.fleetingjobsbackend.common.services.WorkerService;
import com.fleetingtrails.fleetingjobsbackend.document.dto.RequestWorkerGenerateResumeWithUrlDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final WorkerService workerService;

    public byte[] getSampleResume () {
        RequestWorkerGenerateResumeWithUrlDto request = new RequestWorkerGenerateResumeWithUrlDto();
        request.setUrl("https://example.com/");
        return workerService.webClient.post()
                .uri("/documents/test-resume.pdf")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_PDF)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();


    }
}
