package com.fleetingtrails.fleetingjobsbackend.company.dto;

import lombok.Setter;

public class CompanyCreateDto {
    @Setter
    private String name;
    @Setter
    private String listingUrl;
    @Setter
    private String singlePageUrlTemplate;
    @Setter
    private Boolean enabled = true;

    public CompanyCreateDto () { }
}
