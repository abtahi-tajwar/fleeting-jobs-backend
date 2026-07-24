package com.fleetingtrails.fleetingjobsbackend.common.services.rabbit.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ReceiveNewJobListingMessageDto {
    @NonNull
    private Long company_id;
    private String title;
    private String url;
}
