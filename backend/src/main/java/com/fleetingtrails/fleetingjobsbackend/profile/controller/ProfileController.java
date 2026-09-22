package com.fleetingtrails.fleetingjobsbackend.profile.controller;

import com.fleetingtrails.fleetingjobsbackend.profile.dto.ProfileGetResponseDto;
import com.fleetingtrails.fleetingjobsbackend.profile.service.ProfileService;
import com.fleetingtrails.fleetingjobsbackend.user.dto.UserResponseDto;
import com.fleetingtrails.fleetingjobsbackend.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    @GetMapping
    public ResponseEntity<ProfileGetResponseDto> getProfile (
            @AuthenticationPrincipal UserEntity user
    ) {
        return ResponseEntity.ok(profileService.getProfile(user.getId()));
    }
}
