package com.fleetingtrails.fleetingjobsbackend.company.dto;

import lombok.Getter;

public class CompanyGetDto {
    @Getter
    private Long id;
    @Getter
    private String name;
    @Getter
    private String listingUrl;
    @Getter
    private String singlePageUrlTemplate;
    @Getter
    private Boolean enabled = true;

    public CompanyGetDto () { }
}
