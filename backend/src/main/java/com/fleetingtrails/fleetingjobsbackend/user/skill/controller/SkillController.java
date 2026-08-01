package com.fleetingtrails.fleetingjobsbackend.user.skill.controller;

import com.fleetingtrails.fleetingjobsbackend.common.response.APIListResponse;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIPostResponse;
import com.fleetingtrails.fleetingjobsbackend.user.skill.dto.SkillCreateDto;
import com.fleetingtrails.fleetingjobsbackend.user.skill.dto.SkillResponseDto;
import com.fleetingtrails.fleetingjobsbackend.user.skill.dto.SkillUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.user.skill.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @PostMapping
    public ResponseEntity<APIPostResponse<SkillResponseDto>> createSkill(
            @PathVariable Long userId,
            @Valid @RequestBody SkillCreateDto createDto) {
        SkillResponseDto createdSkill = skillService.createSkill(userId, createDto);


        APIPostResponse<SkillResponseDto> response = APIPostResponse.success(createdSkill);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<APIListResponse<SkillResponseDto>> getSkillsByUserId(@PathVariable Long userId) {
        List<SkillResponseDto> skills = skillService.getSkillsByUserId(userId);


        APIListResponse<SkillResponseDto> response = APIListResponse.success(skills);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{skillId}")
    public ResponseEntity<APIPostResponse<SkillResponseDto>> updateSkill(
            @PathVariable Long skillId,
            @Valid @RequestBody SkillUpdateDto updateDto) {
        SkillResponseDto updatedSkill = skillService.updateSkill(skillId, updateDto);

        APIPostResponse<SkillResponseDto> response = APIPostResponse.success(updatedSkill);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long skillId) {
        skillService.deleteSkill(skillId);
        return ResponseEntity.noContent().build();
    }
}