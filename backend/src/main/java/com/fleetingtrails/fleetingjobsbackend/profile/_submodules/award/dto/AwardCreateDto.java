package com.fleetingtrails.fleetingjobsbackend.profile._submodules.award.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AwardCreateDto {
    private String title;
    private String organization;
    private LocalDate date;
    private String description;
}