package com.fleetingtrails.fleetingjobsbackend.profile._submodules.education.controller;

import com.fleetingtrails.fleetingjobsbackend.auth.annotation.Authorize;
import com.fleetingtrails.fleetingjobsbackend.common.AppModule;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIListResponse;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIPostResponse;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.education.dto.EducationCreateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.education.dto.EducationResponseDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.education.dto.EducationUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.education.service.EducationService;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile/education")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @Authorize(
            module = AppModule.PROFILE,
            submodule = AppModule.Submodule.EDUCATION,
            action = "CREATE"
    )
    @PostMapping("/")
    public ResponseEntity<APIPostResponse<EducationResponseDto>> createEducation(
            @AuthenticationPrincipal UserEntity user,
            @Valid @RequestBody EducationCreateDto createDto) {
        EducationResponseDto createdEducation = educationService.createEducation(user.getId(), createDto);
        APIPostResponse<EducationResponseDto> response = APIPostResponse.success(createdEducation);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Authorize(
            module = AppModule.PROFILE,
            submodule = AppModule.Submodule.EDUCATION,
            action = "LIST"
    )
    @GetMapping("/")
    public ResponseEntity<APIListResponse<EducationResponseDto>> getEducationsByUserId(@AuthenticationPrincipal UserEntity user) {
        List<EducationResponseDto> educations = educationService.getEducationsByUserId(user.getId());
        APIListResponse<EducationResponseDto> response = APIListResponse.success(educations);
        return ResponseEntity.ok(response);
    }

    @Authorize(
            module = AppModule.PROFILE,
            submodule = AppModule.Submodule.EDUCATION,
            action = "UPDATE"
    )
    @PutMapping("/{educationId}")
    public ResponseEntity<APIPostResponse<EducationResponseDto>> updateEducation(
            @PathVariable Long educationId,
            @Valid @RequestBody EducationUpdateDto updateDto) {
        EducationResponseDto updatedEducation = educationService.updateEducation(educationId, updateDto);
        APIPostResponse<EducationResponseDto> response = APIPostResponse.success(updatedEducation);
        return ResponseEntity.ok(response);
    }

    @Authorize(
            module = AppModule.PROFILE,
            submodule = AppModule.Submodule.EDUCATION,
            action = "DELETE"
    )
    @DeleteMapping("/{educationId}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long educationId) {
        educationService.deleteEducation(educationId);
        return ResponseEntity.noContent().build();
    }
}
