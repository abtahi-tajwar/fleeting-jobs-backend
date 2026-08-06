package com.fleetingtrails.fleetingjobsbackend.auth.dto;

import com.fleetingtrails.fleetingjobsbackend.user.enums.Role;
import lombok.Data;

@Data
public class AuthResponseDto {
    private String token;
    private Long userId;
    private String email;
    private Role role;
}