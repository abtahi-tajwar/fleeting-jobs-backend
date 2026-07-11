package com.fleetingtrails.fleetingjobsbackend.jobs.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobListItemDto {
    private Long id;
    private String title;
    private String url;
}
