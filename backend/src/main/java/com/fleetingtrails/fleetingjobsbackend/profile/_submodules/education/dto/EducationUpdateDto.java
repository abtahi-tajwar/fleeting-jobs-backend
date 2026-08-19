package com.fleetingtrails.fleetingjobsbackend.profile._submodules.education.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EducationUpdateDto {
    private String institution;
    private String degree;
    private String major;
    private String gpa;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean graduated;
}
