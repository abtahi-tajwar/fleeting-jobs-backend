package com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.controller;

import com.fleetingtrails.fleetingjobsbackend.auth.annotation.Authorize;
import com.fleetingtrails.fleetingjobsbackend.common.AppModule;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIListResponse;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIPostResponse;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.dto.AwardCreateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.dto.AwardResponseDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.dto.AwardUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.service.AwardService;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile/awards")
@RequiredArgsConstructor
public class AwardController {

    private final AwardService awardService;

    @Authorize(
            module = AppModule.PROFILE,
            submodule = AppModule.Submodule.AWARD,
            action = "CREATE"
    )
    @PostMapping("/")
    public ResponseEntity<APIPostResponse<AwardResponseDto>> createAward(
            @AuthenticationPrincipal UserEntity user,
            @Valid @RequestBody AwardCreateDto createDto) {
        AwardResponseDto createdAward = awardService.createAward(user.getId(), createDto);
        APIPostResponse<AwardResponseDto> response = APIPostResponse.success(createdAward);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Authorize(
            module = AppModule.PROFILE,
            submodule = AppModule.Submodule.AWARD,
            action = "LIST"
    )
    @GetMapping("/")
    public ResponseEntity<APIListResponse<AwardResponseDto>> getAwardsByUserId(
            @AuthenticationPrincipal UserEntity user
    ) {
        List<AwardResponseDto> awards = awardService.getAwardsByUserId(user.getId());
        APIListResponse<AwardResponseDto> response = APIListResponse.success(awards);
        return ResponseEntity.ok(response);
    }

    @Authorize(
            module = AppModule.PROFILE,
            submodule = AppModule.Submodule.AWARD,
            action = "UPDATE"
    )
    @PutMapping("/{awardId}")
    public ResponseEntity<APIPostResponse<AwardResponseDto>> updateAward(
            @PathVariable Long awardId,
            @Valid @RequestBody AwardUpdateDto updateDto) {
        AwardResponseDto updatedAward = awardService.updateAward(awardId, updateDto);
        APIPostResponse<AwardResponseDto> response = APIPostResponse.success(updatedAward);
        return ResponseEntity.ok(response);
    }

    @Authorize(
            module = AppModule.PROFILE,
            submodule = AppModule.Submodule.AWARD,
            action = "DELETE"
    )
    @DeleteMapping("/{awardId}")
    public ResponseEntity<Void> deleteAward(@PathVariable Long awardId) {
        awardService.deleteAward(awardId);
        return ResponseEntity.noContent().build();
    }
}