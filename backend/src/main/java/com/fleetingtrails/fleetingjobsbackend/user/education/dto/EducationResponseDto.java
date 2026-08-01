package com.fleetingtrails.fleetingjobsbackend.user.education.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EducationResponseDto {
    private Long id;
    private Long userId;
    private String institution;
    private String degree;
    private String major;
    private String gpa;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean graduated;
}
