package com.fleetingtrails.fleetingjobsbackend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleSignupRequestDto {
    @NotBlank
    private String idToken;
}
