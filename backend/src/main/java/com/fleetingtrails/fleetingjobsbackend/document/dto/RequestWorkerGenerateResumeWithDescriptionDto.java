package com.fleetingtrails.fleetingjobsbackend.document.dto;

import com.fleetingtrails.fleetingjobsbackend.profile.dto.ProfileGetResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestWorkerGenerateResumeWithDescriptionDto {
    private String description;
    private ProfileGetResponseDto profile;
}
