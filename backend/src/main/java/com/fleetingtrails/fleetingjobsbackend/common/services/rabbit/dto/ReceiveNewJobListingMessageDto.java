package com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiveNewJobListingMessageDto {
    private String title;
    private String url;
}
