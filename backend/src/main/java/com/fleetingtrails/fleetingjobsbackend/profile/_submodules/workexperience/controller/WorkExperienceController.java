package com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.controller;

import com.fleetingtrails.fleetingjobsbackend.common.response.APIListResponse;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIPostResponse;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.dto.WorkExperienceCreateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.dto.WorkExperienceResponseDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.dto.WorkExperienceUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.service.WorkExperienceService;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile/experiences")
@RequiredArgsConstructor
public class WorkExperienceController {

    private final WorkExperienceService workExperienceService;

    @PostMapping("/")
    public ResponseEntity<APIPostResponse<WorkExperienceResponseDto>> createWorkExperience(
            @AuthenticationPrincipal UserEntity user,
            @Valid @RequestBody WorkExperienceCreateDto createDto) {
        WorkExperienceResponseDto createdExperience = workExperienceService.createWorkExperience(user.getId(), createDto);
        APIPostResponse<WorkExperienceResponseDto> response = APIPostResponse.success(createdExperience);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<APIListResponse<WorkExperienceResponseDto>> getWorkExperiencesByUserId(@AuthenticationPrincipal UserEntity user) {
        List<WorkExperienceResponseDto> experiences = workExperienceService.getWorkExperiencesByUserId(user.getId());
        APIListResponse<WorkExperienceResponseDto> response = APIListResponse.success(experiences);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{experienceId}")
    public ResponseEntity<APIPostResponse<WorkExperienceResponseDto>> updateWorkExperience(
            @PathVariable Long experienceId,
            @Valid @RequestBody WorkExperienceUpdateDto updateDto) {
        WorkExperienceResponseDto updatedExperience = workExperienceService.updateWorkExperience(experienceId, updateDto);
        APIPostResponse<WorkExperienceResponseDto> response = APIPostResponse.success(updatedExperience);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{experienceId}")
    public ResponseEntity<Void> deleteWorkExperience(@PathVariable Long experienceId) {
        workExperienceService.deleteWorkExperience(experienceId);
        return ResponseEntity.noContent().build();
    }
}