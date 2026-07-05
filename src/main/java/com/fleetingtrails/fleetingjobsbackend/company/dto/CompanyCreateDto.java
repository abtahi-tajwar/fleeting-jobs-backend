package com.fleetingtrails.fleetingjobsbackend.company.dto;

import lombok.Getter;
import lombok.Setter;

public class CompanyCreateDto {
    @Setter
    @Getter
    private String name;
    @Setter
    @Getter
    private String listingUrl;
    @Setter
    @Getter
    private String singlePageUrlTemplate;
    @Setter
    @Getter
    private Boolean enabled = true;

    public CompanyCreateDto () { }
}
