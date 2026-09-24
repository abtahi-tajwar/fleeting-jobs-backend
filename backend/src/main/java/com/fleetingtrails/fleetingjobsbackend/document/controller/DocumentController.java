package com.fleetingtrails.fleetingjobsbackend.document.controller;

import com.fleetingtrails.fleetingjobsbackend.auth.annotation.Authorize;
import com.fleetingtrails.fleetingjobsbackend.common.AppModule;
import com.fleetingtrails.fleetingjobsbackend.document.dto.ResumeFromDescriptionRequestDto;
import com.fleetingtrails.fleetingjobsbackend.document.dto.ResumeFromUrlRequestDto;
import com.fleetingtrails.fleetingjobsbackend.document.service.DocumentService;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @Authorize(
            module = AppModule.DOCUMENT,
            submodule = AppModule.Submodule.DEFAULT,
            action = "GENERATE"
    )
    @PostMapping("/generate/resume/from-url")
    public ResponseEntity<byte[]> generateResumeFromUrl (@RequestBody ResumeFromUrlRequestDto body) {
        byte[] pdf = documentService.generateResumeFromUrl(body);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"resume.pdf\""
                )
                .body(pdf);

    }

    @Authorize(
            module = AppModule.DOCUMENT,
            submodule = AppModule.Submodule.DEFAULT,
            action = "GENERATE"
    )
    @PostMapping("/generate/resume/from-description")
    public ResponseEntity<byte[]> generateResumeFromDescription (
            @RequestBody ResumeFromDescriptionRequestDto body,
            @AuthenticationPrincipal UserEntity user
    ) {

        byte[] pdf = documentService.generateResumeFromDescription(body, user.getId());
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
