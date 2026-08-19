package com.fleetingtrails.fleetingjobsbackend.profile._submodules.education.controller;

import com.fleetingtrails.fleetingjobsbackend.common.response.APIListResponse;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIPostResponse;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.education.dto.EducationCreateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.education.dto.EducationResponseDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.education.dto.EducationUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.education.service.EducationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/education")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @PostMapping
    public ResponseEntity<APIPostResponse<EducationResponseDto>> createEducation(
            @PathVariable Long userId,
            @Valid @RequestBody EducationCreateDto createDto) {
        EducationResponseDto createdEducation = educationService.createEducation(userId, createDto);
        APIPostResponse<EducationResponseDto> response = APIPostResponse.success(createdEducation);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<APIListResponse<EducationResponseDto>> getEducationsByUserId(@PathVariable Long userId) {
        List<EducationResponseDto> educations = educationService.getEducationsByUserId(userId);
        APIListResponse<EducationResponseDto> response = APIListResponse.success(educations);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{educationId}")
    public ResponseEntity<APIPostResponse<EducationResponseDto>> updateEducation(
            @PathVariable Long educationId,
            @Valid @RequestBody EducationUpdateDto updateDto) {
        EducationResponseDto updatedEducation = educationService.updateEducation(educationId, updateDto);
        APIPostResponse<EducationResponseDto> response = APIPostResponse.success(updatedEducation);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{educationId}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long educationId) {
        educationService.deleteEducation(educationId);
        return ResponseEntity.noContent().build();
    }
}
