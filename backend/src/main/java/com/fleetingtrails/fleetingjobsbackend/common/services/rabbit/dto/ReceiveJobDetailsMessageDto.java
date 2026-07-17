package com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiveJobDetailsMessageDto {
    private Long id;
    private String url;
    private String description;
}
