package com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestJobDetailsMessageDto {
    private Long id;
    private String url;
}

