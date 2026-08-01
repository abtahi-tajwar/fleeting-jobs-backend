package com.fleetingtrails.fleetingjobsbackend.user.workexperience.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class WorkExperienceResponseDto {
    private Long id;
    private Long userId;
    private String company;
    private String position;
    private String location;
    private String employmentType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean currentlyWorking;
    private String description;
}