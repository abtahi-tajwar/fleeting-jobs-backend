package com.fleetingtrails.fleetingjobsbackend.auth.dto;

import com.fleetingtrails.fleetingjobsbackend.auth.entity.RoleEntity;
import lombok.Data;

@Data
public class AuthResponseDto {
    private String token;
    private Long userId;
    private String email;
    private RoleEntity role;
    private boolean requiresPasswordSetup;
}