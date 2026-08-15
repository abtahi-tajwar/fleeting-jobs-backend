package com.fleetingtrails.fleetingjobsbackend.user.dto;

import com.fleetingtrails.fleetingjobsbackend.user.enums.Role;
import lombok.Data;

@Data
public class UserUpdateDto {
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
    private Role role;
    private String password;
}