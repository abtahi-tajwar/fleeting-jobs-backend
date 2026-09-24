package com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.controller;

import com.fleetingtrails.fleetingjobsbackend.auth.annotation.Authorize;
import com.fleetingtrails.fleetingjobsbackend.common.AppModule;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIListResponse;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIPostResponse;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.dto.CertificationCreateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.dto.CertificationResponseDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.dto.CertificationUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.certification.service.CertificationService;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;

    @Authorize(
            module = AppModule.PROFILE,
            submodule = AppModule.Submodule.CERTIFICATION,
            action = "CREATE"
    )
    @PostMapping
    public ResponseEntity<APIPostResponse<CertificationResponseDto>> createCertification(
            @AuthenticationPrincipal UserEntity user,
            @Valid @RequestBody CertificationCreateDto createDto) {
        CertificationResponseDto createdCertification = certificationService.createCertification(user.getId(), createDto);
        APIPostResponse<CertificationResponseDto> response = APIPostResponse.success(createdCertification);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Authorize(
            module = AppModule.PROFILE,
            submodule = AppModule.Submodule.CERTIFICATION,
            action = "LIST"
    )
    @GetMapping
    public ResponseEntity<APIListResponse<CertificationResponseDto>> getCertificationsByUserId(@AuthenticationPrincipal UserEntity user) {
        List<CertificationResponseDto> certifications = certificationService.getCertificationsByUserId(user.getId());
        APIListResponse<CertificationResponseDto> response = APIListResponse.success(certifications);
        return ResponseEntity.ok(response);
    }

    @Authorize(
            module = AppModule.PROFILE,
            submodule = AppModule.Submodule.CERTIFICATION,
            action = "UPDATE"
    )
    @PutMapping("/{certificationId}")
    public ResponseEntity<APIPostResponse<CertificationResponseDto>> updateCertification(
            @PathVariable Long certificationId,
            @Valid @RequestBody CertificationUpdateDto updateDto) {
        CertificationResponseDto updatedCertification = certificationService.updateCertification(certificationId, updateDto);
        APIPostResponse<CertificationResponseDto> response = APIPostResponse.success(updatedCertification);
        return ResponseEntity.ok(response);
    }

    @Authorize(
            module = AppModule.PROFILE,
            submodule = AppModule.Submodule.CERTIFICATION,
            action = "DELETE"
    )
    @DeleteMapping("/{certificationId}")
    public ResponseEntity<Void> deleteCertification(@PathVariable Long certificationId) {
        certificationService.deleteCertification(certificationId);
        return ResponseEntity.noContent().build();
    }
}
