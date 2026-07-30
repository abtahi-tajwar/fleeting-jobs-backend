package com.fleetingtrails.fleetingjobsbackend.user.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String linkedin;
    private String github;
    private String portfolioWebsite;
    private String city;
    private String province;
    private String country;
    private String summary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}