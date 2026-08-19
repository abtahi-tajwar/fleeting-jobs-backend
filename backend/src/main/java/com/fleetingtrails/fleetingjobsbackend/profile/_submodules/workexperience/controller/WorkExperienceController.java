package com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.controller;

import com.fleetingtrails.fleetingjobsbackend.common.response.APIListResponse;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIPostResponse;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.dto.WorkExperienceCreateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.dto.WorkExperienceResponseDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.dto.WorkExperienceUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.workexperience.service.WorkExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/experiences")
@RequiredArgsConstructor
public class WorkExperienceController {

    private final WorkExperienceService workExperienceService;

    @PostMapping
    public ResponseEntity<APIPostResponse<WorkExperienceResponseDto>> createWorkExperience(
            @PathVariable Long userId,
            @Valid @RequestBody WorkExperienceCreateDto createDto) {
        WorkExperienceResponseDto createdExperience = workExperienceService.createWorkExperience(userId, createDto);
        APIPostResponse<WorkExperienceResponseDto> response = APIPostResponse.success(createdExperience);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<APIListResponse<WorkExperienceResponseDto>> getWorkExperiencesByUserId(@PathVariable Long userId) {
        List<WorkExperienceResponseDto> experiences = workExperienceService.getWorkExperiencesByUserId(userId);
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