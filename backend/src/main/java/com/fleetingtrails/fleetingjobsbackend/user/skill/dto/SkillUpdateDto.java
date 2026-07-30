package com.fleetingtrails.fleetingjobsbackend.user.skill.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class SkillUpdateDto {
    private String name;
    private String category;

    @Min(1) @Max(10)
    private Integer strength;

    private Double yearsExperience;
    private String notes;
}