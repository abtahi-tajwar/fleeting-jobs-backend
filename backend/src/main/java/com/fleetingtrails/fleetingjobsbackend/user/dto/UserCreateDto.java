package com.fleetingtrails.fleetingjobsbackend.user.dto;

import com.fleetingtrails.fleetingjobsbackend.auth.entity.RoleEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCreateDto {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String phone;
    private String linkedin;
    private String github;
    private String portfolioWebsite;
    private String city;
    private String province;
    private String country;
    private String summary;
    private RoleEntity role;
}