package com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.controller;

import com.fleetingtrails.fleetingjobsbackend.common.response.APIListResponse;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIPostResponse;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.dto.CertificationCreateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.dto.CertificationResponseDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.dto.CertificationUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.service.CertificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping
    public ResponseEntity<APIPostResponse<CertificationResponseDto>> createCertification(
            @PathVariable Long userId,
            @Valid @RequestBody CertificationCreateDto createDto) {
        CertificationResponseDto createdCertification = certificationService.createCertification(userId, createDto);
        APIPostResponse<CertificationResponseDto> response = APIPostResponse.success(createdCertification);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<APIListResponse<CertificationResponseDto>> getCertificationsByUserId(@PathVariable Long userId) {
        List<CertificationResponseDto> certifications = certificationService.getCertificationsByUserId(userId);
        APIListResponse<CertificationResponseDto> response = APIListResponse.success(certifications);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{certificationId}")
    public ResponseEntity<APIPostResponse<CertificationResponseDto>> updateCertification(
            @PathVariable Long certificationId,
            @Valid @RequestBody CertificationUpdateDto updateDto) {
        CertificationResponseDto updatedCertification = certificationService.updateCertification(certificationId, updateDto);
        APIPostResponse<CertificationResponseDto> response = APIPostResponse.success(updatedCertification);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{certificationId}")
    public ResponseEntity<Void> deleteCertification(@PathVariable Long certificationId) {
        certificationService.deleteCertification(certificationId);
        return ResponseEntity.noContent().build();
    }
}
