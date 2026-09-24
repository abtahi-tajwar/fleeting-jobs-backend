package com.fleetingtrails.fleetingjobsbackend.profile._submodules.skill.controller;

import com.fleetingtrails.fleetingjobsbackend.auth.annotation.Authorize;
import com.fleetingtrails.fleetingjobsbackend.common.AppModule;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIListResponse;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIPostResponse;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.skill.dto.SkillCreateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.skill.dto.SkillResponseDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.skill.dto.SkillUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.skill.service.SkillService;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;


    @Authorize(
            module = AppModule.PROFILE,
            submodule = AppModule.Submodule.SKILL,
            action = "CREATE"
    )
    @PostMapping("/")
    public ResponseEntity<APIPostResponse<SkillResponseDto>> createSkill(
            @Valid @RequestBody SkillCreateDto createDto,
            @AuthenticationPrincipal UserEntity user
    ) {
        SkillResponseDto createdSkill = skillService.createSkill(user.getId(), createDto);


        APIPostResponse<SkillResponseDto> response = APIPostResponse.success(createdSkill);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Authorize(
            module = AppModule.PROFILE,
            submodule = AppModule.Submodule.SKILL,
            action = "LIST"
    )
    @GetMapping("/")
    public ResponseEntity<APIListResponse<SkillResponseDto>> getSkillsByUserId(
            @AuthenticationPrincipal UserEntity user
    ) {
        List<SkillResponseDto> skills = skillService.getSkillsByUserId(user.getId());


        APIListResponse<SkillResponseDto> response = APIListResponse.success(skills);
        return ResponseEntity.ok(response);
    }

    @Authorize(
            module = AppModule.PROFILE,
            submodule = AppModule.Submodule.SKILL,
            action = "UPDATE"
    )
    @PutMapping("/{skillId}")
    public ResponseEntity<APIPostResponse<SkillResponseDto>> updateSkill(
            @PathVariable Long skillId,
            @Valid @RequestBody SkillUpdateDto updateDto) {
        SkillResponseDto updatedSkill = skillService.updateSkill(skillId, updateDto);

        APIPostResponse<SkillResponseDto> response = APIPostResponse.success(updatedSkill);
        return ResponseEntity.ok(response);
    }

    @Authorize(
            module = AppModule.PROFILE,
            submodule = AppModule.Submodule.SKILL,
            action = "DELETE"
    )
    @DeleteMapping("/{skillId}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long skillId) {
        skillService.deleteSkill(skillId);
        return ResponseEntity.noContent().build();
    }
}