package com.fleetingtrails.fleetingjobsbackend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfirmEmailRequestDto {
    @NotBlank
    private String token;
}
