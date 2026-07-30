package com.fleetingtrails.fleetingjobsbackend.user.workexperience.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class WorkExperienceCreateDto {
    private String company;
    private String position;
    private String location;
    private String employmentType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean currentlyWorking;
    private String description;
}
