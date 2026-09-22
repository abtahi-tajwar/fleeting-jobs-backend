package com.fleetingtrails.fleetingjobsbackend.document.service;

import com.fleetingtrails.fleetingjobsbackend.common.services.WorkerService;
import com.fleetingtrails.fleetingjobsbackend.document.dto.RequestWorkerGenerateResumeWithDescriptionDto;
import com.fleetingtrails.fleetingjobsbackend.document.dto.RequestWorkerGenerateResumeWithUrlDto;
import com.fleetingtrails.fleetingjobsbackend.document.dto.ResumeFromDescriptionRequestDto;
import com.fleetingtrails.fleetingjobsbackend.document.dto.ResumeFromUrlRequestDto;
import com.fleetingtrails.fleetingjobsbackend.profile.dto.ProfileGetResponseDto;
import com.fleetingtrails.fleetingjobsbackend.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final WorkerService workerService;
    private final ProfileService profileService;

    public byte[] generateResumeFromUrl (ResumeFromUrlRequestDto body) {
        RequestWorkerGenerateResumeWithUrlDto request = new RequestWorkerGenerateResumeWithUrlDto();
        request.setUrl(body.getUrl());
        return workerService.webClient.post()
                .uri("/documents/generate/from_url/resume.pdf")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_PDF)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();


    }
    public byte[] generateResumeFromDescription (ResumeFromDescriptionRequestDto body, long userId) {
        ProfileGetResponseDto profile = profileService.getProfile(userId);

        RequestWorkerGenerateResumeWithDescriptionDto request = new RequestWorkerGenerateResumeWithDescriptionDto();
        request.setDescription(body.getDescription());
        request.setProfile(profile);

        return workerService.webClient.post()
                .uri("/documents/generate/from_description/resume.pdf")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_PDF)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();


    }
}
