package com.fleetingtrails.fleetingjobsbackend.user.award.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AwardCreateDto {
    private String title;
    private String organization;
    private LocalDate date;
    private String description;
}