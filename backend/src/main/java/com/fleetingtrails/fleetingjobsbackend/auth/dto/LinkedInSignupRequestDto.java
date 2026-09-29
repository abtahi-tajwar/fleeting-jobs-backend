package com.fleetingtrails.fleetingjobsbackend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LinkedInSignupRequestDto {
    @NotBlank
    private String accessToken;
}
