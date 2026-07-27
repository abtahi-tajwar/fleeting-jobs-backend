package com.fleetingtrails.fleetingjobsbackend.document.controller;

import com.fleetingtrails.fleetingjobsbackend.document.dto.ResumeFromLinkRequestDto;
import com.fleetingtrails.fleetingjobsbackend.document.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping("/generate/resume/from-link")
    public ResponseEntity<byte[]> generateSampleResume (@RequestBody ResumeFromLinkRequestDto body) {
        byte[] pdf = documentService.generateResumeFromLink(body);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"resume.pdf\""
                )
                .body(pdf);

    }
}
