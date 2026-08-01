package com.fleetingtrails.fleetingjobsbackend.user.award.controller;

import com.fleetingtrails.fleetingjobsbackend.common.response.APIListResponse;
import com.fleetingtrails.fleetingjobsbackend.common.response.APIPostResponse;
import com.fleetingtrails.fleetingjobsbackend.user.award.dto.AwardCreateDto;
import com.fleetingtrails.fleetingjobsbackend.user.award.dto.AwardResponseDto;
import com.fleetingtrails.fleetingjobsbackend.user.award.dto.AwardUpdateDto;
import com.fleetingtrails.fleetingjobsbackend.user.award.service.AwardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/awards")
@RequiredArgsConstructor
public class AwardController {

    private final AwardService awardService;

    @PostMapping
    public ResponseEntity<APIPostResponse<AwardResponseDto>> createAward(
            @PathVariable Long userId,
            @Valid @RequestBody AwardCreateDto createDto) {
        AwardResponseDto createdAward = awardService.createAward(userId, createDto);
        APIPostResponse<AwardResponseDto> response = APIPostResponse.success(createdAward);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<APIListResponse<AwardResponseDto>> getAwardsByUserId(@PathVariable Long userId) {
        List<AwardResponseDto> awards = awardService.getAwardsByUserId(userId);
        APIListResponse<AwardResponseDto> response = APIListResponse.success(awards);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{awardId}")
    public ResponseEntity<APIPostResponse<AwardResponseDto>> updateAward(
            @PathVariable Long awardId,
            @Valid @RequestBody AwardUpdateDto updateDto) {
        AwardResponseDto updatedAward = awardService.updateAward(awardId, updateDto);
        APIPostResponse<AwardResponseDto> response = APIPostResponse.success(updatedAward);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{awardId}")
    public ResponseEntity<Void> deleteAward(@PathVariable Long awardId) {
        awardService.deleteAward(awardId);
        return ResponseEntity.noContent().build();
    }
}