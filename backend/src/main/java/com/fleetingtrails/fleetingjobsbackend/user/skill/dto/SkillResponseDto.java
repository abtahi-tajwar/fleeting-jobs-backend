package com.fleetingtrails.fleetingjobsbackend.user.skill.dto;

import lombok.Data;

@Data
public class SkillResponseDto {
    private Long id;
    private Long userId;
    private String name;
    private String category;
    private Integer strength;
    private Double yearsExperience;
    private String notes;
}