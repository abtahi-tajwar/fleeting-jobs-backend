package com.fleetingtrails.fleetingjobsbackend.auth.dto;

import com.fleetingtrails.fleetingjobsbackend.auth.entity.PermissionEntity;
import com.fleetingtrails.fleetingjobsbackend.auth.entity.RoleEntity;
import lombok.Data;

import java.util.List;

@Data
public class AuthResponseDto {
    private String token;
    private Long userId;
    private String email;
    private RoleEntity role;
    private List<PermissionEntity> permissions;
    private boolean requiresPasswordSetup;
}